package com.coding2themax.petcore.pet.service.profile.api.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Pet")
class PetTest {

  private Pet pet;

  @BeforeEach
  void setUp() {
    pet = new Pet();
  }

  @Nested
  @DisplayName("identity")
  class Identity {

    @Test
    @DisplayName("isNew() returns true when id is null")
    void isNewWhenIdIsNull() {
      assertThat(pet.isNew()).isTrue();
    }

    @Test
    @DisplayName("isNew() returns false when id is set")
    void isNotNewWhenIdIsSet() {
      pet.setId(UUID.randomUUID());
      assertThat(pet.isNew()).isFalse();
    }

    @Test
    @DisplayName("setAsNewPet() forces isNew() to return true even when id is set")
    void setAsNewPetForcesIsNew() {
      pet.setId(UUID.randomUUID());
      pet.setAsNewPet();
      assertThat(pet.isNew()).isTrue();
    }

    @Test
    @DisplayName("setAsNewPet() returns the same Pet instance for fluent chaining")
    void setAsNewPetReturnsSelf() {
      Pet result = pet.setAsNewPet();
      assertThat(result).isSameAs(pet);
    }

    @Test
    @DisplayName("getId() / setId() round-trip")
    void idRoundTrip() {
      UUID id = UUID.randomUUID();
      pet.setId(id);
      assertThat(pet.getId()).isEqualTo(id);
    }
  }

  @Nested
  @DisplayName("basic fields")
  class BasicFields {

    @Test
    @DisplayName("name round-trip")
    void name() {
      pet.setName("Buddy");
      assertThat(pet.getName()).isEqualTo("Buddy");
    }

    @Test
    @DisplayName("species round-trip")
    void species() {
      pet.setSpecies(Species.DOG);
      assertThat(pet.getSpecies()).isEqualTo(Species.DOG);
    }

    @Test
    @DisplayName("breed round-trip")
    void breed() {
      pet.setBreed("Labrador");
      assertThat(pet.getBreed()).isEqualTo("Labrador");
    }

    @Test
    @DisplayName("sex round-trip")
    void sex() {
      pet.setSex(Sex.MALE);
      assertThat(pet.getSex()).isEqualTo(Sex.MALE);
    }

    @Test
    @DisplayName("size round-trip")
    void size() {
      pet.setSize(Size.LARGE);
      assertThat(pet.getSize()).isEqualTo(Size.LARGE);
    }

    @Test
    @DisplayName("intakeDate round-trip")
    void intakeDate() {
      LocalDate date = LocalDate.of(2025, 6, 15);
      pet.setIntakeDate(date);
      assertThat(pet.getIntakeDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("intakeType round-trip")
    void intakeType() {
      pet.setIntakeType(IntakeType.STRAY);
      assertThat(pet.getIntakeType()).isEqualTo(IntakeType.STRAY);
    }

    @Test
    @DisplayName("status round-trip")
    void status() {
      pet.setStatus(PetStatus.AVAILABLE);
      assertThat(pet.getStatus()).isEqualTo(PetStatus.AVAILABLE);
    }

    @Test
    @DisplayName("externalReferenceId round-trip")
    void externalReferenceId() {
      pet.setExternalReferenceId("EXT-001");
      assertThat(pet.getExternalReferenceId()).isEqualTo("EXT-001");
    }

    @Test
    @DisplayName("createdAt round-trip")
    void createdAt() {
      Instant now = Instant.now();
      pet.setCreatedAt(now);
      assertThat(pet.getCreatedAt()).isEqualTo(now);
    }
  }

  @Nested
  @DisplayName("age delegation")
  class AgeDelegation {

    @Test
    @DisplayName("setAge() stores value and unit; getAge() reconstructs them")
    void ageRoundTrip() {
      Age age = new Age(3, AgeUnit.YEARS);
      pet.setAge(age);
      Age result = pet.getAge();
      assertThat(result.value()).isEqualTo(3);
      assertThat(result.unit()).isEqualTo(AgeUnit.YEARS);
    }

    @Test
    @DisplayName("setAge() with MONTHS unit")
    void ageInMonths() {
      pet.setAge(new Age(6, AgeUnit.MONTHS));
      assertThat(pet.getAge()).isEqualTo(new Age(6, AgeUnit.MONTHS));
    }

    @Test
    @DisplayName("setAge() with WEEKS unit")
    void ageInWeeks() {
      pet.setAge(new Age(8, AgeUnit.WEEKS));
      assertThat(pet.getAge()).isEqualTo(new Age(8, AgeUnit.WEEKS));
    }

    @Test
    @DisplayName("setAge() with DAYS unit")
    void ageInDays() {
      pet.setAge(new Age(10, AgeUnit.DAYS));
      assertThat(pet.getAge()).isEqualTo(new Age(10, AgeUnit.DAYS));
    }

    @Test
    @DisplayName("setAge() with zero value is valid")
    void ageZero() {
      pet.setAge(new Age(0, AgeUnit.DAYS));
      assertThat(pet.getAge().value()).isZero();
    }
  }

  @Nested
  @DisplayName("Age record validation")
  class AgeRecordValidation {

    @Test
    @DisplayName("Age record throws IllegalArgumentException for negative value")
    void negativeAgeThrows() {
      assertThatThrownBy(() -> new Age(-1, AgeUnit.YEARS))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Age value cannot be negative");
    }

    @Test
    @DisplayName("Age record allows zero value")
    void zeroAgeIsValid() {
      Age age = new Age(0, AgeUnit.DAYS);
      assertThat(age.value()).isZero();
    }
  }

  @Nested
  @DisplayName("fully populated pet")
  class FullyPopulated {

    @Test
    @DisplayName("all fields set and retrievable")
    void allFields() {
      UUID id = UUID.randomUUID();
      Instant now = Instant.now();
      LocalDate intakeDate = LocalDate.of(2025, 1, 10);

      pet.setId(id);
      pet.setName("Luna");
      pet.setSpecies(Species.CAT);
      pet.setBreed("Siamese");
      pet.setSex(Sex.FEMALE);
      pet.setAge(new Age(2, AgeUnit.YEARS));
      pet.setSize(Size.SMALL);
      pet.setIntakeDate(intakeDate);
      pet.setIntakeType(IntakeType.OWNER_SURRENDER);
      pet.setStatus(PetStatus.AVAILABLE);
      pet.setExternalReferenceId("EXT-CAT-099");
      pet.setCreatedAt(now);

      assertThat(pet.getId()).isEqualTo(id);
      assertThat(pet.getName()).isEqualTo("Luna");
      assertThat(pet.getSpecies()).isEqualTo(Species.CAT);
      assertThat(pet.getBreed()).isEqualTo("Siamese");
      assertThat(pet.getSex()).isEqualTo(Sex.FEMALE);
      assertThat(pet.getAge()).isEqualTo(new Age(2, AgeUnit.YEARS));
      assertThat(pet.getSize()).isEqualTo(Size.SMALL);
      assertThat(pet.getIntakeDate()).isEqualTo(intakeDate);
      assertThat(pet.getIntakeType()).isEqualTo(IntakeType.OWNER_SURRENDER);
      assertThat(pet.getStatus()).isEqualTo(PetStatus.AVAILABLE);
      assertThat(pet.getExternalReferenceId()).isEqualTo("EXT-CAT-099");
      assertThat(pet.getCreatedAt()).isEqualTo(now);
      assertThat(pet.isNew()).isFalse();
    }
  }
}
