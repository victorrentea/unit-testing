Feature: Tennis Game

  Scenario: New Game
    Given A new tennis game
    Then Score is "Love-Love"


  Scenario: New Game
    Given A new tennis game
    When Player1 scores
    Then Score is "Fifteen-Love"

  Scenario: New Game
    Given A new tennis game
    When Player1 scores
    And Player2 scores
    Then Score is "Fifteen-Fifteen"

  Scenario: Deuce
    Given A new tennis game
    When Player1 scores 3 points
    And Player2 scores 3 points
    Then Score is "Deuce"

  Scenario Outline: Tabel, sa planga bizu
    Given A new tennis game
    When Player1 scores <player1Points> points
    And Player2 scores <player2Points> points
    Then Score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore    |
      | 0             | 0             | Love-Love        |
      | 1             | 0             | Fifteen-Love     |
      | 7             | 5             | Game won Player1 |