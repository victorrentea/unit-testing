Feature: Tennis Game

  Background:
    Given A customer with name, email and address city

  Scenario: 1
    When That customer name is missing
    Then Validation should fail

  Scenario: 2
    When That customer email is missing
    Then Validation should fail
