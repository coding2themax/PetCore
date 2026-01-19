package com.coding2themax.petcore.pet.service.profile.application;

import org.springframework.stereotype.Service;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.api.service.PetIntakeService;
import com.coding2themax.petcore.pet.service.profile.infrastructure.PetRepository;

import reactor.core.publisher.Mono;

@Service
public class PetIntakeServicePostgres implements PetIntakeService {

  private final PetRepository petRepository;

  public PetIntakeServicePostgres(PetRepository petRepository) {
    this.petRepository = petRepository;
  }

  @Override
  public Mono<Pet> createPetProfile(PetIntakeRequest request) {
    Pet pet = new Pet();

    return petRepository.save(pet);
  }
}
