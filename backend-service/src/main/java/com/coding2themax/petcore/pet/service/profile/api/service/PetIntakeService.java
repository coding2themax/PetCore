package com.coding2themax.petcore.pet.service.profile.api.service;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.api.dto.response.PetResponse;

import reactor.core.publisher.Mono;

public interface PetIntakeService {

  Mono<PetResponse> createPetProfile(PetIntakeRequest request);

}