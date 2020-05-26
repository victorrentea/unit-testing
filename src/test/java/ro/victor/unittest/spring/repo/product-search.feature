@txn
Feature: Search for product

Scenario: Search by name finds data
  Given A product exists in database
  And That product name is "aXb"
  When Search criteria name is "x"
  Then That product is returned
