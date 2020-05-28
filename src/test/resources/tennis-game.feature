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
      | player1Points | player2Points | expectedScore     |
      | 0             | 0             | Love-Love         |
      | 1             | 0             | Fifteen-Love      |
      | 0             | 1             | Love-Fifteen      |
      | 1             | 1             | Fifteen-Fifteen   |
      | 2             | 0             | Thirty-Love       |
      | 3             | 0             | Forty-Love        |
      | 3             | 3             | Deuce             |
      | 4             | 4             | Deuce             |
      | 8             | 8             | Deuce             |
      | 4             | 3             | Advantage Player1 |
      | 5             | 6             | Advantage Player2 |
      | 8             | 6             | Game won Player1  |
      | 0             | 4             | Game won Player2  |
      | 1             | 4             | Game won Player2  |

