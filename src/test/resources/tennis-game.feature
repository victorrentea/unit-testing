Feature:

  Background: #runs before each Scenario bellow
    Given an empty game

  Scenario: Empty Game
    Then the score is "Love-Love"

  Scenario: Empty Game
    When Player1 scores
    Then the score is "Fifteen-Love"

  Scenario: Fifteen Fifteen
    When both players scores
    Then the score is "Fifteen-Fifteen"

  Scenario: Fifteen Thirty
    When Player1 scores 1 point
    When Player2 scores 2 points
    Then the score is "Fifteen-Thirty"

  Scenario Template: All
    When Player1 scores <player1Points> point
    When Player2 scores <player2Points> points
    Then the score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore  |
      | 1             | 2             | Fifteen-Thirty |
      | 4             | 4             | Deuce          |
