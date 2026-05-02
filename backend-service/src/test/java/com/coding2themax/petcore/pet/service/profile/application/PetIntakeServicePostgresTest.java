package com.coding2themax.petcore.pet.service.profile.application;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Age;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.AgeUnit;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.IntakeType;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.PetStatus;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Sex;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Size;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Species;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.api.dto.response.PetResponse;
import com.coding2themax.petcore.pet.service.profile.api.service.PetIntakeServicePostgres;
import com.coding2themax.petcore.pet.service.profile.infrastructure.PetRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class PetIntakeServicePostgresTest {

  @Mock
  private PetRepository petRepository;

  private PetIntakeRequest request;

  @InjectMocks
  private PetIntakeServicePostgres petIntakeServicePostgres;

  @Test
  void testCreatePetProfile_newPet() {
    request = new PetIntakeRequest(
        "Buddy",
        Species.DOG,
        "Golden Retriever",
        Sex.MALE,
        new Age(3, AgeUnit.YEARS),
        Size.LARGE,
        LocalDate.now(),
        IntakeType.STRAY,
        PetStatus.AVAILABLE,
        "EXT-12345",
        "IDP-12345");

    UUID id = UUID.randomUUID();
    Instant createdAt = Instant.now();

    Pet saved = new Pet();
    saved.setId(id);
    saved.setName(request.name());
    saved.setSpecies(request.species());
    saved.setBreed(request.breed());
    saved.setSex(request.sex());
    saved.setAge(request.age());
    saved.setSize(request.size());
    saved.setIntakeDate(request.intakeDate());
    saved.setIntakeType(request.intakeType());
    saved.setStatus(request.status());
    saved.setCreatedAt(createdAt);

    BDDMockito.when(petRepository.findByIdempotencyKey("IDP-12345"))
        .thenReturn(Mono.empty());
    BDDMockito.when(petRepository.save(BDDMockito.any()))
        .thenReturn(Mono.just(saved));

    StepVerifier.create(petIntakeServicePostgres.createPetProfile(request))
        .assertNext(resp -> {
          Assertions.assertAll(() -> {
            assert resp.petId().equals(id);
            assert resp.name().equals(request.name());
            assert resp.species().equalsIgnoreCase(request.species().name());
            assert resp.breed().equals(request.breed());
            assert resp.status().equalsIgnoreCase(request.status().name());
            assert resp.createdAt().equals(createdAt);
          });
        })
        .verifyComplete();

    BDDMockito.verify(petRepository).findByIdempotencyKey("IDP-12345");
    BDDMockito.verify(petRepository).save(BDDMockito.any());
  }

  @Test
  void testCreatePetProfile_existingPet_returnsExisting() {
    request = new PetIntakeRequest(
        "Buddy",
        Species.DOG,
        "Golden Retriever",
        Sex.MALE,
        new Age(3, AgeUnit.YEARS),
        Size.LARGE,
        LocalDate.now(),
        IntakeType.STRAY,
        PetStatus.AVAILABLE,
        "EXT-12345",
        "IDP-12345");

    UUID existingId = UUID.randomUUID();
    Instant createdAt = Instant.now();

    Pet existing = new Pet();
    existing.setId(existingId);
    existing.setName("Buddy");
    existing.setSpecies(Species.DOG);
    existing.setBreed("Golden Retriever");
    existing.setStatus(PetStatus.AVAILABLE);
    existing.setCreatedAt(createdAt);

    BDDMockito.when(petRepository.findByIdempotencyKey("IDP-12345"))
        .thenReturn(Mono.just(existing));

    StepVerifier.create(petIntakeServicePostgres.createPetProfile(request))
        .assertNext(resp -> {
          Assertions.assertAll(
              () -> Assertions.assertEquals(existingId, resp.petId()),
              () -> Assertions.assertEquals("Buddy", resp.name()),
              () -> Assertions.assertEquals(PetStatus.AVAILABLE.name(), resp.status()));
        })
        .verifyComplete();

    BDDMockito.verify(petRepository).findByIdempotencyKey("IDP-12345");
    BDDMockito.verify(petRepository, BDDMockito.never()).save(BDDMockito.any());
  }

  @Test
  void testGetPetById_found() {
    UUID id = UUID.randomUUID();
    Instant createdAt = Instant.now();

    Pet stored = new Pet();
    stored.setId(id);
    stored.setName("Buddy");
    stored.setSpecies(Species.DOG);
    stored.setBreed("Golden Retriever");
    stored.setSex(Sex.MALE);
    stored.setAge(new Age(3, AgeUnit.YEARS));
    stored.setSize(Size.LARGE);
    stored.setIntakeDate(LocalDate.now());
    stored.setIntakeType(IntakeType.STRAY);
    stored.setStatus(PetStatus.AVAILABLE);
    stored.setCreatedAt(createdAt);

    BDDMockito.when(petRepository.findById(id))
        .thenReturn(Mono.just(stored));

    StepVerifier.create(petIntakeServicePostgres.getPetById(id.toString()))
        .assertNext((PetResponse resp) -> {
          Assertions.assertAll(
              () -> Assertions.assertEquals(id, resp.petId()),
              () -> Assertions.assertEquals("Buddy", resp.name()),
              () -> Assertions.assertEquals(Species.DOG.name(), resp.species()),
              () -> Assertions.assertEquals("Golden Retriever", resp.breed()),
              () -> Assertions.assertEquals(PetStatus.AVAILABLE.name(), resp.status()),
              () -> Assertions.assertEquals(createdAt, resp.createdAt()));
        })
        .verifyComplete();

    BDDMockito.verify(petRepository).findById(id);
  }

  @Test
  void testGetPetById_notFound() {
    UUID id = UUID.randomUUID();

    BDDMockito.when(petRepository.findById(id))
        .thenReturn(Mono.empty());

    StepVerifier.create(petIntakeServicePostgres.getPetById(id.toString()))
        .expectErrorSatisfies(error -> {
          Assertions.assertInstanceOf(ResponseStatusException.class, error);
          Assertions.assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) error).getStatusCode());
        })
        .verify();

    BDDMockito.verify(petRepository).findById(id);
  }

  @Test
  void testGetPetById_invalidUuid_throwsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> petIntakeServicePostgres.getPetById("not-a-uuid"));
  }

  @Test
  void testGetAllPets_noFilter_returnsAllPets() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Instant createdAt = Instant.now();

    Pet pet1 = new Pet();
    pet1.setId(id1);
    pet1.setName("Buddy");
    pet1.setSpecies(Species.DOG);
    pet1.setBreed("Golden Retriever");
    pet1.setStatus(PetStatus.AVAILABLE);
    pet1.setCreatedAt(createdAt);

    Pet pet2 = new Pet();
    pet2.setId(id2);
    pet2.setName("Whiskers");
    pet2.setSpecies(Species.CAT);
    pet2.setBreed("Tabby");
    pet2.setStatus(PetStatus.ADOPTED);
    pet2.setCreatedAt(createdAt);

    BDDMockito.when(petRepository.findAll())
        .thenReturn(Flux.just(pet1, pet2));

    StepVerifier.create(petIntakeServicePostgres.getAllPets(null, null))
        .assertNext(resp -> Assertions.assertAll(
            () -> Assertions.assertEquals(id1, resp.petId()),
            () -> Assertions.assertEquals("Buddy", resp.name()),
            () -> Assertions.assertEquals(Species.DOG.name(), resp.species()),
            () -> Assertions.assertEquals("Golden Retriever", resp.breed()),
            () -> Assertions.assertEquals(PetStatus.AVAILABLE.name(), resp.status()),
            () -> Assertions.assertEquals(createdAt, resp.createdAt())))
        .assertNext(resp -> Assertions.assertAll(
            () -> Assertions.assertEquals(id2, resp.petId()),
            () -> Assertions.assertEquals("Whiskers", resp.name()),
            () -> Assertions.assertEquals(Species.CAT.name(), resp.species())))
        .verifyComplete();

    BDDMockito.verify(petRepository).findAll();
  }

  @Test
  void testGetAllPets_filterBySpecies_callsFindBySpecies() {
    UUID id = UUID.randomUUID();
    Instant createdAt = Instant.now();

    Pet pet = new Pet();
    pet.setId(id);
    pet.setName("Whiskers");
    pet.setSpecies(Species.CAT);
    pet.setBreed("Tabby");
    pet.setStatus(PetStatus.FOSTER);
    pet.setCreatedAt(createdAt);

    BDDMockito.when(petRepository.findBySpecies(Species.CAT))
        .thenReturn(Flux.just(pet));

    StepVerifier.create(petIntakeServicePostgres.getAllPets("CAT", null))
        .assertNext(resp -> Assertions.assertAll(
            () -> Assertions.assertEquals(id, resp.petId()),
            () -> Assertions.assertEquals(Species.CAT.name(), resp.species()),
            () -> Assertions.assertEquals(PetStatus.FOSTER.name(), resp.status())))
        .verifyComplete();

    BDDMockito.verify(petRepository).findBySpecies(Species.CAT);
    BDDMockito.verify(petRepository, BDDMockito.never()).findAll();
  }

  @Test
  void testGetAllPets_filterByStatus_callsFindByStatus() {
    UUID id = UUID.randomUUID();
    Instant createdAt = Instant.now();

    Pet pet = new Pet();
    pet.setId(id);
    pet.setName("Buddy");
    pet.setSpecies(Species.DOG);
    pet.setBreed("Labrador");
    pet.setStatus(PetStatus.FOSTER);
    pet.setCreatedAt(createdAt);

    BDDMockito.when(petRepository.findByStatus(PetStatus.FOSTER))
        .thenReturn(Flux.just(pet));

    StepVerifier.create(petIntakeServicePostgres.getAllPets(null, "FOSTER"))
        .assertNext(resp -> Assertions.assertAll(
            () -> Assertions.assertEquals(id, resp.petId()),
            () -> Assertions.assertEquals(PetStatus.FOSTER.name(), resp.status())))
        .verifyComplete();

    BDDMockito.verify(petRepository).findByStatus(PetStatus.FOSTER);
    BDDMockito.verify(petRepository, BDDMockito.never()).findAll();
  }

  @Test
  void testGetAllPets_filterBySpeciesAndStatus_callsFindBySpeciesAndStatus() {
    UUID id = UUID.randomUUID();
    Instant createdAt = Instant.now();

    Pet pet = new Pet();
    pet.setId(id);
    pet.setName("Whiskers");
    pet.setSpecies(Species.CAT);
    pet.setBreed("Tabby");
    pet.setStatus(PetStatus.FOSTER);
    pet.setCreatedAt(createdAt);

    BDDMockito.when(petRepository.findBySpeciesAndStatus(Species.CAT, PetStatus.FOSTER))
        .thenReturn(Flux.just(pet));

    StepVerifier.create(petIntakeServicePostgres.getAllPets("CAT", "FOSTER"))
        .assertNext(resp -> Assertions.assertAll(
            () -> Assertions.assertEquals(id, resp.petId()),
            () -> Assertions.assertEquals(Species.CAT.name(), resp.species()),
            () -> Assertions.assertEquals(PetStatus.FOSTER.name(), resp.status())))
        .verifyComplete();

    BDDMockito.verify(petRepository).findBySpeciesAndStatus(Species.CAT, PetStatus.FOSTER);
    BDDMockito.verify(petRepository, BDDMockito.never()).findAll();
  }

  @Test
  void testGetAllPets_empty_returnsEmptyFlux() {
    BDDMockito.when(petRepository.findAll())
        .thenReturn(Flux.empty());

    StepVerifier.create(petIntakeServicePostgres.getAllPets(null, null))
        .verifyComplete();

    BDDMockito.verify(petRepository).findAll();
  }
}
