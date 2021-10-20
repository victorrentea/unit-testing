Feature: Tennis Game

  Scenario: New Game
    Given A new tennis game
    Then Score is "Love-All"

  Scenario: Un punct
    Given A new tennis game
    When Player "ONE" scores 1 point
    Then Score is "Fifteen-Love"

  Scenario: Un punct la fiecare
    Given A new tennis game
    When Player "ONE" scores 1 point
    And Player "TWO" scores 1 point
    Then Score is "Fifteen-All"

  Scenario Outline: Template
    Given A new tennis game
#    And User microservice returns John|Doe|2021-01-01|
#    And Supplier id 13 is in database
    When Player "ONE" scores <player1Points> point
    And Player "TWO" scores <player2Points> point
    Then Score is "<expectedScore>"
#    And A Kafka message is sent with contents containing 13

    Examples:
      | player1Points | player2Points | expectedScore      |
      | 0             | 0             | Love-All     |
      | 1             | 1             | Fifteen-All     |
      | 1             | 2             | Fifteen-Thirty     |
      | 2             | 2             | Thirty-All      |
      | 0             | 3             | Love-Forty         |
      | 3             | 3             | Deuce              |
      | 4             | 4             | Deuce              |
      | 3             | 4             | Advantage Player 2 |
      | 4             | 3             | Advantage Player 1 |
      | 4             | 2             | Game won Player 1 |
      | 4             | 1             | Game won Player 1 |
      | 5             | 3             | Game won Player 1 |
