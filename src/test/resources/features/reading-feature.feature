Feature: Posting to a personal timeline

  Background: Alice is logged in
    Given Alice has an account
    And Bob has an account
    And Bob is logged in

  Scenario: I can see messages of others in their timeline
    Given Alice posted message "X"
    When Bob checks Alice's timeline
    Then Bob sees message "X"

  Scenario: I can see messages of those I follow in my feed
    Given Alice posted message "X"
    And Bob follows Alice
    When Bob checks his feed
    Then Bob sees message "X"

  Scenario: I can see messages of those I follow in my feed in the order they were posted
    Given Alice posted message "X" on "2021-10-01"
    And Charlie posted message "Y" on "2021-10-02"
    And Bob follows Alice
    And Bob follows Charlie
    When Bob checks his feed
    Then Bob sees messages "Y,X"

  Scenario Outline: I can see messages of those I follow in my feed in the order they were posted
    Given Alice posted message "<firstPost>" on "<firstPostDate>"
    And "Charlie" posted message "<secondPost>" on "<secondPostDate>"
    And Bob follows Alice
    And Bob follows Charlie
    When Bob checks his feed
    Then Bob sees messages "<expectedMessages>"

    Examples:
      | firstPost | firstPostDate | secondPost | secondPostDate | expectedMessage |
      | X         | 2021-06-01    | Y          | 2021-12-01     | Y               |
      | X         | 2021-10-01    | Y          | 2021-12-01     | Y,X               |