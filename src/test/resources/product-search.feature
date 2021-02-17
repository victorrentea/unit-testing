@txn
Feature: Search Product
  Scenario: Fara criterii
    Given One product exists
    Then That product is returned by search

  Scenario: 
    Given One product exists
    And That product has name "Copac"
    And The search criteria name is "Opa"
    Then That product is returned by search

# Scenario Outline:
#    Given One product exists
#    And That product has name "<productName>"
#    And That product has category "WIFE"
#    And The search criteria name is "<criteriaName>"
#    Then That product is returned by search
#
#   Examples:
