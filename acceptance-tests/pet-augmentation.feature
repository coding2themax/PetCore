Feature: AI augmentation controls
  As a system operator
  I want AI usage to be controlled and auditable
  So that cost and risk are minimized

  Scenario: Disable AI augmentation via feature flag
    Given AI augmentation is disabled
    When a downstream service requests AI assistance
    Then the request should be rejected gracefully
    And a non-AI response should be returned

  Scenario: Enforce AI usage limits
    Given AI augmentation is enabled
    And the daily token limit has been reached
    When a new AI request is made
    Then the request should be blocked
    And the event should be logged for audit purposes

  Scenario: Log all AI interactions
    Given AI augmentation is enabled
    When an AI request is processed
    Then the request and response metadata should be logged
    And sensitive data should not be persisted
