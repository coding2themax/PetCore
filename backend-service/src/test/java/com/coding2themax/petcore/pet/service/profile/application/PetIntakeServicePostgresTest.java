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

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Age;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.AgeUnit;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.IntakeType;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.PetStatus;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Sex;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Size;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Species;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.infrastructure.PetRepository;

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
  void testCreatePetProfile() {
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
        "EXT-12345");

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

    BDDMockito.when(petRepository.save(BDDMockito.any()))
        .thenReturn(Mono.just(saved));
    StepVerifier.create(
        petIntakeServicePostgres.createPetProfile(request))
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
    BDDMockito.verify(petRepository).save(BDDMockito.any());
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
        .assertNext(pet -> {
          Assertions.assertAll(
              () -> Assertions.assertEquals(id, pet.getId()),
              () -> Assertions.assertEquals("Buddy", pet.getName()),
              () -> Assertions.assertEquals(Species.DOG, pet.getSpecies()),
              () -> Assertions.assertEquals("Golden Retriever", pet.getBreed()),
              () -> Assertions.assertEquals(Sex.MALE, pet.getSex()),
              () -> Assertions.assertEquals(new Age(3, AgeUnit.YEARS), pet.getAge()),
              () -> Assertions.assertEquals(Size.LARGE, pet.getSize()),
              () -> Assertions.assertEquals(IntakeType.STRAY, pet.getIntakeType()),
              () -> Assertions.assertEquals(PetStatus.AVAILABLE, pet.getStatus()),
              () -> Assertions.assertEquals(createdAt, pet.getCreatedAt()));
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
        .verifyComplete();

    BDDMockito.verify(petRepository).findById(id);
  }

  @Test
  void testGetPetById_invalidUuid_throwsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> petIntakeServicePostgres.getPetById("not-a-uuid"));
  }
}
