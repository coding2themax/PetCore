Feature: Pet intake
  As a shelter staff member
  I want to create pet profiles through an intake process
  So that pets can be matched and discovered reliably

  Background:
    Given I am an authenticated shelter staff user

  Scenario: Successfully create a new pet profile
    Given valid pet intake information
    When I submit the pet intake form
    Then a new pet profile should be created
    And the pet profile should be persisted
    And a unique pet identifier should be returned
    And the response status should be 201 Created

  Scenario: Reject pet intake with missing required fields
    Given pet intake information is missing required fields
    When I submit the pet intake form
    Then the request should be rejected
    And validation errors should be returned
    And no pet profile should be persisted
    And the response status should be 400 Bad Request

  Scenario: Reject pet intake with invalid attribute values
    Given pet intake information contains invalid values
    When I submit the pet intake form
    Then the request should be rejected
    And validation error messages should identify invalid fields
    And no pet profile should be persisted

  Scenario: Prevent duplicate pet intake submission
    Given a pet intake submission was already processed
    When the same intake request is submitted again
    Then the existing pet profile should be returned
    And no duplicate pet profile should be created

  Scenario: Intake succeeds when AI augmentation is disabled
    Given AI augmentation is disabled
    And valid pet intake information
    When I submit the pet intake form
    Then the pet profile should be created successfully
    And the intake flow should not depend on AI services
