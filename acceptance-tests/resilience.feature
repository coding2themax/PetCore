Feature: System resilience and isolation
  As a platform owner
  I want failures to be isolated
  So that core functionality remains available

  Scenario: Matching service remains available when AI fails
    Given the AI provider is unavailable
    When a compatibility match is requested
    Then the matching service should return a rule-based result
    And no error should be exposed to the end user

  Scenario: Search service remains available when profile service is slow
    Given cached profile data is available
    When a search request is made
    Then the search service should return results
    And degraded dependencies should be logged
