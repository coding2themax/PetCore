package com.coding2themax.petcore.pet.service.profile.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.infrastructure.PetRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class PetIntakeServicePostgresTest {

  @Mock
  private PetRepository petRepository;

  @Mock
  private PetIntakeRequest request;

  @InjectMocks
  private PetIntakeServicePostgres petIntakeServicePostgres;

  @Test
  void testCreatePetProfile() {
    BDDMockito.when(petRepository.save(BDDMockito.any()))
        .thenReturn(Mono.empty());
    StepVerifier.create(
        petIntakeServicePostgres.createPetProfile(request))
        .verifyComplete();
    BDDMockito.verify(petRepository).save(BDDMockito.any());
  }
}
