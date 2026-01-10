Feature: Pet profile management
  As a shelter staff member
  I want to create and manage pet profiles
  So that pets can be matched and discovered reliably

  Scenario: Create a new pet profile
    Given I am an authenticated shelter user
    When I submit a valid pet intake form
    Then a pet profile should be created
    And the profile should be stored in the pet profile database
    And the response should include a unique pet identifier

  Scenario: Reject invalid pet intake data
    Given I am an authenticated shelter user
    When I submit a pet intake form with missing required fields
    Then the request should be rejected
    And a validation error should be returned

  Scenario: Retrieve an existing pet profile
    Given a pet profile exists
    When I request the pet profile by ID
    Then the pet profile details should be returned
    And no other services should be modified
