@txn
Feature: Search for product

  Background:
    Given Supplier "X" e in baza
    And Supplier "Y" exists

  Scenario:
    Given One product exists
    And That product has name "name"
    And That product has supplier "X"
    And That product has category "PT_MINE"
    When The search criteria name is "name"
    And The search criteria supplier is "X"
    And The search criteria category is "PT_MINE"
    Then That product is returned by search

  Scenario:
    Given One product exists
    And That product has name "name"
    When The search criteria name is "namex"
    Then No products are returned by search




  Scenario Template:
    Given One product exists
    And That product has name "<productName>"
    When The search criteria name is "<criteriaName>"
    Then That product is returned by search

    Examples:
    | productName | criteriaName |
    | namE        | e            |
    | namEx        | e            |
    | namEx        | E            |
    | namEx        | n            |



  Scenario:
    Given One product exists
    And That product has name "namE"
    When The search criteria name is "e"
    Then That product is returned by search

  Scenario:
    Given One product exists
    And That product has name "namEx"
    When The search criteria name is "e"
    Then That product is returned by search






  Scenario:
    Given One product exists
    Then That product is returned by search
