package com.coding2themax.petcore.pet.service.profile.api.handler.validator;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Age;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.AgeUnit;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.IntakeType;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.PetStatus;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Sex;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Size;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Species;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;

public class PetIntakeValidatorTest {

  private PetIntakeValidator validator;

  @BeforeEach
  void setUp() {
    validator = new PetIntakeValidator();
  }

  private PetIntakeRequest validRequest() {
    return new PetIntakeRequest(
        "Buddy",
        Species.DOG,
        "Labrador",
        Sex.MALE,
        new Age(3, AgeUnit.YEARS),
        Size.LARGE,
        LocalDate.now(),
        IntakeType.STRAY,
        PetStatus.AVAILABLE,
        "EXT-001",
        "IDP-001");
  }

  private Errors errorsFor(PetIntakeRequest request) {
    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "petIntakeRequest");
    validator.validate(request, errors);
    return errors;
  }

  // --- supports() ---

  @Test
  @DisplayName("supports() returns true for PetIntakeRequest")
  void supports_petIntakeRequest_returnsTrue() {
    Assertions.assertTrue(validator.supports(PetIntakeRequest.class));
  }

  @Test
  @DisplayName("supports() returns false for unrelated class")
  void supports_otherClass_returnsFalse() {
    Assertions.assertFalse(validator.supports(String.class));
  }

  // --- valid request ---

  @Test
  @DisplayName("valid request produces no errors")
  void validate_validRequest_noErrors() {
    Errors errors = errorsFor(validRequest());
    Assertions.assertFalse(errors.hasErrors());
  }

  // --- name ---

  @Test
  @DisplayName("null name produces field error")
  void validate_nullName_hasError() {
    PetIntakeRequest request = new PetIntakeRequest(
        null, Species.DOG, "Labrador", Sex.MALE,
        new Age(3, AgeUnit.YEARS), Size.LARGE, LocalDate.now(),
        IntakeType.STRAY, PetStatus.AVAILABLE, null, null);
    Errors errors = errorsFor(request);
    Assertions.assertTrue(errors.hasFieldErrors("name"));
  }

  @Test
  @DisplayName("blank name produces field error")
  void validate_blankName_hasError() {
    PetIntakeRequest request = new PetIntakeRequest(
        "   ", Species.DOG, "Labrador", Sex.MALE,
        new Age(3, AgeUnit.YEARS), Size.LARGE, LocalDate.now(),
        IntakeType.STRAY, PetStatus.AVAILABLE, null, null);
    Errors errors = errorsFor(request);
    Assertions.assertTrue(errors.hasFieldErrors("name"));
  }

  // --- species ---

  @Test
  @DisplayName("null species produces field error")
  void validate_nullSpecies_hasError() {
    PetIntakeRequest request = new PetIntakeRequest(
        "Buddy", null, "Labrador", Sex.MALE,
        new Age(3, AgeUnit.YEARS), Size.LARGE, LocalDate.now(),
        IntakeType.STRAY, PetStatus.AVAILABLE, null, null);
    Errors errors = errorsFor(request);
    Assertions.assertTrue(errors.hasFieldErrors("species"));
  }

  // --- sex ---

  @Test
  @DisplayName("null sex produces field error")
  void validate_nullSex_hasError() {
    PetIntakeRequest request = new PetIntakeRequest(
        "Buddy", Species.DOG, "Labrador", null,
        new Age(3, AgeUnit.YEARS), Size.LARGE, LocalDate.now(),
        IntakeType.STRAY, PetStatus.AVAILABLE, null, null);
    Errors errors = errorsFor(request);
    Assertions.assertTrue(errors.hasFieldErrors("sex"));
  }

  // --- intakeType ---

  @Test
  @DisplayName("null intakeType produces field error")
  void validate_nullIntakeType_hasError() {
    PetIntakeRequest request = new PetIntakeRequest(
        "Buddy", Species.DOG, "Labrador", Sex.MALE,
        new Age(3, AgeUnit.YEARS), Size.LARGE, LocalDate.now(),
        null, PetStatus.AVAILABLE, null, null);
    Errors errors = errorsFor(request);
    Assertions.assertTrue(errors.hasFieldErrors("intakeType"));
  }

  // --- status ---

  @Test
  @DisplayName("null status produces field error")
  void validate_nullStatus_hasError() {
    PetIntakeRequest request = new PetIntakeRequest(
        "Buddy", Species.DOG, "Labrador", Sex.MALE,
        new Age(3, AgeUnit.YEARS), Size.LARGE, LocalDate.now(),
        IntakeType.STRAY, null, null, null);
    Errors errors = errorsFor(request);
    Assertions.assertTrue(errors.hasFieldErrors("status"));
  }

  // --- intakeDate ---

  @Test
  @DisplayName("null intakeDate produces field error")
  void validate_nullIntakeDate_hasError() {
    PetIntakeRequest request = new PetIntakeRequest(
        "Buddy", Species.DOG, "Labrador", Sex.MALE,
        new Age(3, AgeUnit.YEARS), Size.LARGE, null,
        IntakeType.STRAY, PetStatus.AVAILABLE, null, null);
    Errors errors = errorsFor(request);
    Assertions.assertTrue(errors.hasFieldErrors("intakeDate"));
  }

  @Test
  @DisplayName("future intakeDate produces field error")
  void validate_futureIntakeDate_hasError() {
    PetIntakeRequest request = new PetIntakeRequest(
        "Buddy", Species.DOG, "Labrador", Sex.MALE,
        new Age(3, AgeUnit.YEARS), Size.LARGE, LocalDate.now().plusDays(1),
        IntakeType.STRAY, PetStatus.AVAILABLE, null, null);
    Errors errors = errorsFor(request);
    Assertions.assertTrue(errors.hasFieldErrors("intakeDate"));
  }

  @Test
  @DisplayName("today intakeDate is valid")
  void validate_todayIntakeDate_noError() {
    Errors errors = errorsFor(validRequest());
    Assertions.assertFalse(errors.hasFieldErrors("intakeDate"));
  }

  // --- age unit ---

  @Test
  @DisplayName("age with null unit produces field error")
  void validate_ageWithNullUnit_hasError() {
    PetIntakeRequest request = new PetIntakeRequest(
        "Buddy", Species.DOG, "Labrador", Sex.MALE,
        new Age(3, null), Size.LARGE, LocalDate.now(),
        IntakeType.STRAY, PetStatus.AVAILABLE, null, null);
    Errors errors = errorsFor(request);
    Assertions.assertTrue(errors.hasFieldErrors("age"));
  }

  @Test
  @DisplayName("null age itself produces no age error")
  void validate_nullAge_noAgeError() {
    PetIntakeRequest request = new PetIntakeRequest(
        "Buddy", Species.DOG, "Labrador", Sex.MALE,
        null, Size.LARGE, LocalDate.now(),
        IntakeType.STRAY, PetStatus.AVAILABLE, null, null);
    Errors errors = errorsFor(request);
    Assertions.assertFalse(errors.hasFieldErrors("age"));
  }
}
