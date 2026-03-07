package com.coding2themax.petcore.pet.service.profile.api.handler.validator;

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
    // TODO Auto-generated method stub
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "Name is required");
  }

}
