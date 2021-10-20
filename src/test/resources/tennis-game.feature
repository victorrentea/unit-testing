Feature: Tennis Game

  Scenario: New Game
    Given A new tennis game
    Then Score is "Love-Love"

  Scenario: Un punct
    Given A new tennis game
    When Player "ONE" scores 1 point
    Then Score is "Fifteen-Love"

  Scenario: Un punct la fiecare
    Given A new tennis game
    When Player "ONE" scores 1 point
    And Player "TWO" scores 1 point
    Then Score is "Fifteen-Fifteen"

  Scenario Outline: Template
    Given A new tennis game
    When Player "ONE" scores <player1Points> point
    And Player "TWO" scores <player2Points> point
    Then Score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore  |
      | 2             | 3             | Fifteen-Thirty |
      | 3             | 3             | Thirty-Thirty  |
