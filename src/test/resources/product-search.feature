@txn
Feature: tot
  Scenario: Fara Criterii
    Given One product exists
    And That product has name "Copac"
    When The search criteria name is "pA"
    Then That product is returned by search
  Scenario: Fara Criterii2 bis
    Given One product exists
    And That product has name "Copac"
    When The search criteria name is "pA"
    Then That product is returned by search