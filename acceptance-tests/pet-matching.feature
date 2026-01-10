Feature: Pet matching and classification
  As an adopter
  I want to receive compatible pet matches
  So that I can make informed adoption decisions

  Scenario: Match a pet using rule-based scoring
    Given a pet profile exists
    And deterministic matching rules are configured
    When I request a compatibility match
    Then rule-based scoring should be applied
    And a confidence score should be returned
    And the result should not require AI

  Scenario: Provide AI explanation when confidence is low
    Given a pet profile exists
    And rule-based matching confidence is below the threshold
    And AI augmentation is enabled
    When I request a compatibility match
    Then rule-based scoring should be applied first
    And an AI-generated explanation may be included
    And the AI output should not override rule results

  Scenario: Continue matching when AI is disabled
    Given a pet profile exists
    And AI augmentation is disabled
    When I request a compatibility match
    Then rule-based scoring should be applied
    And a valid match result should still be returned
