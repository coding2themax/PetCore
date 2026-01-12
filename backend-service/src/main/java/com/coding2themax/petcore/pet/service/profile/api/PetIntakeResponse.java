package com.coding2themax.petcore.pet.service.profile.api;

import com.coding2themax.petcore.pet.service.profile.domain.*;

import java.time.LocalDate;
import java.util.UUID;

public record PetIntakeResponse(
    UUID petId,
    String name,
    Species species,
    String breed,
    Sex sex,
    Age age,
    Size size,
    LocalDate intakeDate,
    IntakeType intakeType,
    PetStatus status) {
}
