package com.coding2themax.petcore.pet.service.profile.application;

import com.coding2themax.petcore.pet.service.profile.domain.*;
import com.coding2themax.petcore.pet.service.profile.infrastructure.PetRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PetIntakeService {

  private final PetRepository petRepository;

  public PetIntakeService(PetRepository petRepository) {
    this.petRepository = petRepository;
  }

  public Mono<Pet> createPetProfile(PetIntakeRequest request) {
    Pet pet = new Pet(
        request.name(),
        request.species(),
        request.breed(),
        request.sex(),
        request.age(),
        request.size(),
        request.intakeDate(),
        request.intakeType(),
        request.status());

    return petRepository.save(pet);
  }
}
