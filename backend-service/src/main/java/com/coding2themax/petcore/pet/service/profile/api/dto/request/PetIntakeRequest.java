package com.coding2themax.petcore.pet.service.profile.api.dto.request;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Age;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.IntakeType;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.PetStatus;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Sex;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Size;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Species;

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
    PetStatus status,
    String externalReferenceId) {
}
