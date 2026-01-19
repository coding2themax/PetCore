package com.coding2themax.petcore.pet.service.profile.api;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.api.service.PetIntakeService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/pets")
public class PetIntakeController {

  private final PetIntakeService petIntakeService;

  public PetIntakeController(PetIntakeService petIntakeService) {
    this.petIntakeService = petIntakeService;
  }

  @PostMapping("/intake")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<PetIntakeResponse> createPetIntake(@RequestBody PetIntakeRequest request) {
    return petIntakeService.createPetProfile(request)
        .map(pet -> new PetIntakeResponse(
            pet.getId(),
            pet.getName(),
            pet.getSpecies(),
            pet.getBreed(),
            pet.getSex(),
            pet.getAge(),
            pet.getSize(),
            pet.getIntakeDate(),
            pet.getIntakeType(),
            pet.getStatus()));
  }
}
