package com.coding2themax.petcore.pet.service.profile.api.handler.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;

@Component
public class PetIntakeValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return clazz.isAssignableFrom(PetIntakeRequest.class);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PetIntakeRequest request = (PetIntakeRequest) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "Name is required");

    if (request.species() == null) {
      errors.rejectValue("species", "field.required", "Species is required");
    }

    if (request.sex() == null) {
      errors.rejectValue("sex", "field.required", "Sex is required");
    }

    if (request.intakeType() == null) {
      errors.rejectValue("intakeType", "field.required", "Intake type is required");
    }

    if (request.status() == null) {
      errors.rejectValue("status", "field.required", "Status is required");
    }

    if (request.intakeDate() == null) {
      errors.rejectValue("intakeDate", "field.required", "Intake date is required");
    } else if (request.intakeDate().isAfter(LocalDate.now())) {
      errors.rejectValue("intakeDate", "field.invalid", "Intake date cannot be in the future");
    }

    if (request.age() != null && request.age().unit() == null) {
      errors.rejectValue("age", "field.invalid", "Age unit is required when age is provided");
    }
  }

}
