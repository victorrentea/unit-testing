@txn
Feature: Search for product

  Background:
    Given Supplier "X" exists
    And Supplier "Y" exists

  Scenario: Search product by exact name and supplier
    Given One product exists
    And That product has name "name"
    And That product has supplier "X"
    When The search criteria name is "name"
    And The search criteria supplier is "X"
    Then That product is returned by search

  Scenario: Product is not found if name does not match
    Given One product exists
    And That product has name "name"
    When The search criteria name is "namex"
    Then No products are returned by search

  Scenario Outline: Multiple Search examples
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