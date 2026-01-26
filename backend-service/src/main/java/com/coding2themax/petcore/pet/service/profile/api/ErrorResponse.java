package com.coding2themax.petcore.pet.service.profile.api;

import java.time.Instant;

/**
 * Standard error response DTO matching the OAS specification.
 * Used for consistent error reporting across the API.
 */
public record ErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path) {
}
