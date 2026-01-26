package com.coding2themax.petcore.pet.service.profile.api.service;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;

import reactor.core.publisher.Mono;

public interface PetIntakeService {

  Mono<Pet> createPetProfile(PetIntakeRequest request);

}