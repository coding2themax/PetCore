package com.coding2themax.petcore.pet.service.profile.api.handler;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.api.dto.response.PetResponse;
import com.coding2themax.petcore.pet.service.profile.api.service.PetIntakeService;

import reactor.core.publisher.Mono;

@Component
public class PetIntakeHandler {

  private static final Logger LOGGER = Logger.getLogger(PetIntakeHandler.class.getName());

  private final PetIntakeService petIntakeService;

  private final Validator validator;

  public PetIntakeHandler(PetIntakeService petIntakeService, Validator validator) {
    this.petIntakeService = petIntakeService;
    this.validator = validator;
  }

  public Mono<ServerResponse> handleIntake(ServerRequest request) {

    Mono<PetIntakeRequest> petMono = request.bodyToMono(PetIntakeRequest.class).map(body -> {
      Errors errors = new BeanPropertyBindingResult(body, "petIntakeRequest");
      validator.validate(body, errors);
      if (errors.hasErrors()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pet intake request");
      }
      return body;
    });
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

  public Mono<ServerResponse> handleGetPetById(ServerRequest request) {
    // Implementation for handling get pet by id request
    String petId = request.pathVariable("id");
    LOGGER.info("Handling get pet by id request for petId: " + petId);

    // Placeholder response
    // return ServerResponse.ok().body(Mono.just("Get Pet By ID Endpoint for petId:
    // " + petId), String.class);
    return petIntakeService.getPetById(petId)
        .flatMap(pet -> {
          PetResponse response = new PetResponse(
              pet.petId(),
              pet.name(),
              pet.species(),
              pet.breed(),
              pet.status(),
              pet.createdAt());
          return ServerResponse.ok().bodyValue(response);
        })
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> handleGetPets(ServerRequest request) {
    // Implementation for handling get pets request
    LOGGER.info("Handling get pets request");

    // Placeholder responsex
    return ServerResponse.ok().body(Mono.just("Get Pets Endpoint"), String.class);
  }
}
