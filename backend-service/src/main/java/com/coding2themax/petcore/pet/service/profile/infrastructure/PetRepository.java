package com.coding2themax.petcore.pet.service.profile.infrastructure;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.PetStatus;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Species;

import java.util.UUID;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PetRepository extends ReactiveCrudRepository<Pet, UUID> {

  Mono<Pet> findByExternalReferenceId(String externalReferenceId);

  Mono<Pet> findByIdempotencyKey(String idempotencyKey);

  Flux<Pet> findBySpecies(Species species);

  Flux<Pet> findByStatus(PetStatus status);

  Flux<Pet> findBySpeciesAndStatus(Species species, PetStatus status);

}
