Feature: Posting to a personal timeline
  Background: Alice is logged in
    Given Alice has an account
    And She is logged in

  Scenario: Basic post
    When She posts a message
    Then The message is visible in her personal timeline

  Scenario: Rejects duplicated messages
    Given She posted a message "X" 5 seconds ago
    When She posts a message "X"
    Then The post is rejected

