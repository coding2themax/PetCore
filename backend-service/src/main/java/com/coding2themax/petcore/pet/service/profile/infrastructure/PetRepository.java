package com.coding2themax.petcore.pet.service.profile.infrastructure;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;

import java.util.UUID;

@Repository
public interface PetRepository extends ReactiveCrudRepository<Pet, UUID> {

}
