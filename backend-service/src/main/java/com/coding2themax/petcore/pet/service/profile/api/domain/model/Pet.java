package com.coding2themax.petcore.pet.service.profile.api.domain.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Table("pets")
public class Pet {

  @Id
  private UUID id;
  private String name;
  private Species species;
  private String breed;
  private Sex sex;
  private int ageValue;
  private AgeUnit ageUnit;
  private Size size;
  private LocalDate intakeDate;
  private IntakeType intakeType;
  private PetStatus status;
  private String externalReferenceId;

  @CreatedDate
  private Instant createdAt;

  // Default constructor for Spring Data
  public Pet() {
  }

  public Pet(String name, Species species, String breed, Sex sex, Age age,
      Size size, LocalDate intakeDate, IntakeType intakeType, PetStatus status,
      String externalReferenceId) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.species = species;
    this.breed = breed;
    this.sex = sex;
    this.ageValue = age.value();
    this.ageUnit = age.unit();
    this.size = size;
    this.intakeDate = intakeDate;
    this.intakeType = intakeType;
    this.status = status;
    this.externalReferenceId = externalReferenceId;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Species getSpecies() {
    return species;
  }

  public void setSpecies(Species species) {
    this.species = species;
  }

  public String getBreed() {
    return breed;
  }

  public void setBreed(String breed) {
    this.breed = breed;
  }

  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public Age getAge() {
    return new Age(ageValue, ageUnit);
  }

  public void setAge(Age age) {
    this.ageValue = age.value();
    this.ageUnit = age.unit();
  }

  public Size getSize() {
    return size;
  }

  public void setSize(Size size) {
    this.size = size;
  }

  public LocalDate getIntakeDate() {
    return intakeDate;
  }

  public void setIntakeDate(LocalDate intakeDate) {
    this.intakeDate = intakeDate;
  }

  public IntakeType getIntakeType() {
    return intakeType;
  }

  public void setIntakeType(IntakeType intakeType) {
    this.intakeType = intakeType;
  }

  public PetStatus getStatus() {
    return status;
  }

  public void setStatus(PetStatus status) {
    this.status = status;
  }

  public String getExternalReferenceId() {
    return externalReferenceId;
  }

  public void setExternalReferenceId(String externalReferenceId) {
    this.externalReferenceId = externalReferenceId;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
