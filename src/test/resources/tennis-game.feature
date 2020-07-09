Feature: Tennis Game

  Scenario: An Empty Game
    Given An empty game
    Then The score is "Love - Love"

  Scenario: FL
    Given An empty game
    When Player1 scores 1 point
    Then The score is "Fifteen - Love"

  Scenario: Adv
    Given An empty game
    When Player1 scores 5 point
    And Player2 scores 4 point
    Then The score is "Advantage Player1"


  Scenario Template: Tate
    Given An empty game
    When Player1 scores <player1Points> point
    And Player2 scores <player2Points> point
    Then The score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore    |
      | 1             | 2             | Fifteen - Thirty |
      | 7             | 7             | Deuce            |