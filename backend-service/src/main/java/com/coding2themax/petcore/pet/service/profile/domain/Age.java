package com.coding2themax.petcore.pet.service.profile.domain;

public record Age(
    int value,
    AgeUnit unit) {
  public Age {
    if (value < 0) {
      throw new IllegalArgumentException("Age value cannot be negative");
    }
  }
}
