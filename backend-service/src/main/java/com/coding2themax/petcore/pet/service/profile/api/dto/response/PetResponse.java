package com.coding2themax.petcore.pet.service.profile.api.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * Simplified pet response DTO matching the OAS specification.
 * Used for search results and basic pet information retrieval.
 */
public record PetResponse(
        UUID petId,
        String name,
        String species,
        String breed,
        String status,
        Instant createdAt) {
}
