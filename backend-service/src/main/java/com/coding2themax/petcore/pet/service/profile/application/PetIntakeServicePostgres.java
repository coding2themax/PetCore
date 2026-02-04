package com.coding2themax.petcore.pet.service.profile.application;

import java.util.UUID;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.api.dto.response.PetResponse;
import com.coding2themax.petcore.pet.service.profile.api.service.PetIntakeService;
import com.coding2themax.petcore.pet.service.profile.infrastructure.PetRepository;

import reactor.core.publisher.Mono;

@Service
public class PetIntakeServicePostgres implements PetIntakeService {

  private static final Logger LOGGER = Logger.getLogger(PetIntakeServicePostgres.class.getName());

  private final PetRepository petRepository;

  public PetIntakeServicePostgres(PetRepository petRepository) {
    this.petRepository = petRepository;
  }

  @Override
  @Transactional
  public Mono<PetResponse> createPetProfile(PetIntakeRequest request) {
    Pet pet = new Pet();
    pet.setName(request.name());
    pet.setSpecies(request.species());
    pet.setBreed(request.breed());
    pet.setSex(request.sex());
    pet.setAge(request.age());
    pet.setSize(request.size());
    pet.setIntakeDate(request.intakeDate());
    pet.setIntakeType(request.intakeType());
    pet.setStatus(request.status());

    pet.setId(UUID.randomUUID());

    return petRepository.save(pet).map(savedPet -> new PetResponse(
        savedPet.getId(),
        savedPet.getName(),
        savedPet.getSpecies().toString(),
        savedPet.getBreed(),
        savedPet.getStatus().toString(),
        savedPet.getCreatedAt()))
        .doOnSuccess(savedPet -> LOGGER.info("Pet profile created with ID: " + savedPet.petId()))
        .doOnError(error -> LOGGER.severe("Error creating pet profile: " + error.getMessage()));
  }
}
