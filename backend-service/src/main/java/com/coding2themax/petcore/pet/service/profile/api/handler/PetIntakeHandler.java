package com.coding2themax.petcore.pet.service.profile.api.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.api.dto.response.PetResponse;
import com.coding2themax.petcore.pet.service.profile.api.service.PetIntakeService;

import reactor.core.publisher.Mono;

@Component
public class PetIntakeHandler {

  private final PetIntakeService petIntakeService;

  public PetIntakeHandler(PetIntakeService petIntakeService) {
    this.petIntakeService = petIntakeService;
  }

  public Mono<ServerResponse> handleIntake(ServerRequest request) {

    Mono<PetIntakeRequest> petMono = request.bodyToMono(PetIntakeRequest.class);
    // Implementation for handling pet intake

    petMono.flatMap(petIntakeService::createPetProfile).flatMap(createdPet -> {
      // Additional processing if needed
      PetResponse petResponse = new PetResponse(createdPet.getId(),
          createdPet.getName(),
          createdPet.getSpecies().toString(),
          createdPet.getBreed(),
          createdPet.getStatus().toString(),
          createdPet.getCreatedAt());
      return Mono.just(petResponse);
    });

    return ServerResponse.ok().body(petMono, Pet.class);
  }
}
