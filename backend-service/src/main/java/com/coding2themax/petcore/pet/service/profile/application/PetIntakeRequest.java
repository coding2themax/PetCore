package com.coding2themax.petcore.pet.service.profile.application;

import com.coding2themax.petcore.pet.service.profile.domain.*;

import java.time.LocalDate;

public record PetIntakeRequest(
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
