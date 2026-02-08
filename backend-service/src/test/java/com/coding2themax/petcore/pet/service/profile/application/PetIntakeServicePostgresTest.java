package com.coding2themax.petcore.pet.service.profile.application;

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
import com.coding2themax.petcore.pet.service.profile.api.dto.response.PetResponse;
import com.coding2themax.petcore.pet.service.profile.infrastructure.PetRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

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
}
