Feature: Tennis Game

  Scenario: New Game
    Given A new tennis game
    Then Score is "Love-Love"

  Scenario: Un punct
    Given A new tennis game
    When Player "ONE" scores 1 points
    Then Score is "Fifteen-Love"

  Scenario: Un punct la fiecare
    Given A new tennis game
    When Player "ONE" scores 1 points
    And Player "TWO" scores 1 points
    Then Score is "Fifteen-Fifteen"

  Scenario Outline: Template
    Given A new tennis game
    When Player "ONE" scores <player1Points> points
    And Player "TWO" scores <player2Points> points
    Then Score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore      |
      | 0             | 0             | Love-Love          |
      | 1             | 1             | Fifteen-Fifteen    |
      | 1             | 2             | Fifteen-Thirty     |
      | 2             | 2             | Thirty-Thirty      |
      | 0             | 3             | Love-Forty         |
      | 3             | 3             | Deuce              |
      | 4             | 4             | Deuce              |
      | 3             | 4             | Advantage Player2 |
      | 4             | 3             | Advantage Player1 |
      | 4             | 2             | Game won Player1  |
      | 4             | 1             | Game won Player1  |
      | 5             | 3             | Game won Player1  |
