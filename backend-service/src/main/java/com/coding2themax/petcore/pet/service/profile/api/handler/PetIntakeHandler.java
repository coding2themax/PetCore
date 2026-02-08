package com.coding2themax.petcore.pet.service.profile.api.handler;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.api.dto.response.PetResponse;
import com.coding2themax.petcore.pet.service.profile.api.service.PetIntakeService;

import reactor.core.publisher.Mono;

@Component
public class PetIntakeHandler {

  private static final Logger LOGGER = Logger.getLogger(PetIntakeHandler.class.getName());

  private final PetIntakeService petIntakeService;

  public PetIntakeHandler(PetIntakeService petIntakeService) {
    this.petIntakeService = petIntakeService;
  }

  public Mono<ServerResponse> handleIntake(ServerRequest request) {

    Mono<PetIntakeRequest> petMono = request.bodyToMono(PetIntakeRequest.class);
    LOGGER.info("Handling pet intake request");

    return petMono.flatMap(petRequest -> {
      return petIntakeService.createPetProfile(petRequest)
          .flatMap(createdPet -> {
            PetResponse response = new PetResponse(
                createdPet.petId(),
                createdPet.name(),
                createdPet.species(),
                createdPet.breed(),
                createdPet.status(),
                createdPet.createdAt());
            return ServerResponse.ok().bodyValue(response);
          });
    });

  }

  public Mono<ServerResponse> handleGetPets(ServerRequest request) {
    // Implementation for handling get pets request
    LOGGER.info("Handling get pets request");

    // Placeholder response
    return ServerResponse.ok().body(Mono.just("Get Pets Endpoint"), String.class);
  }
}
