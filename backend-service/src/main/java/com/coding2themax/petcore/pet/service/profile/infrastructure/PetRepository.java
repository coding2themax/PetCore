package com.coding2themax.petcore.pet.service.profile.infrastructure;

import com.coding2themax.petcore.pet.service.profile.domain.Pet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PetRepository extends ReactiveCrudRepository<Pet, UUID> {
}
