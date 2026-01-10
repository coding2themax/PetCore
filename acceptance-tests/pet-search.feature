Feature: Pet search and discovery
  As a prospective adopter
  I want to search for pets using filters and keywords
  So that I can quickly find suitable matches

  Scenario: Perform keyword-based pet search
    Given pet profiles are indexed for search
    When I search using keywords and filters
    Then keyword-based ranking should be applied
    And results should be returned in ranked order
    And the response time should meet performance targets

  Scenario: Re-rank search results using AI
    Given keyword search results are available
    And semantic re-ranking is enabled
    When I perform a search with ambiguous keywords
    Then AI-based re-ranking may be applied
    And the original ranking should remain available

  Scenario: Search continues when AI is unavailable
    Given keyword search is operational
    And AI re-ranking is unavailable
    When I perform a search
    Then keyword-based results should still be returned
