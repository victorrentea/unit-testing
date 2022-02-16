@txn
Feature: Search for product

  Background:
    Given Supplier "X" exists
    And Supplier "Y" exists


  Scenario:
    Given One product exists
    And That product has name "name"
    And That product has supplier "X"

    When The search criteria name is "name"
    And The search criteria supplier is "X"

    Then That product is returned by search

  Scenario:
    Given One product exists
    And That product has name "name"
    And That product has supplier "X"

    When The search criteria name is "name"
    And The search criteria supplier is "Y"

    Then That product is NOT returned by search

  Scenario:
    Given One product exists
    And That product has name "name"
    When The search criteria name is "namex"
    Then No products are returned by search

  Scenario Outline:
    Given One product exists
    And That product has name "<name>"
    And That product has supplier "<supplier>"
    When The search criteria name is "<searchName>"
    And The search criteria supplier is "<searchSupplier>"
    Then That product is returned by search: "<found>"

    Examples:
      | name       | supplier | searchName | searchSupplier | found |
      | Tree       | X        | Tree       | X              | true  |
      | Tree       | X        | Tree       | Y              | false |
      | Zorro Mask |          | Mask       |                | true  |