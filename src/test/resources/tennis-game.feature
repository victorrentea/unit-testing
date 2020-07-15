Feature: Tennis Game

  Scenario: An empty game
    Given An empty game
    Then The score is "Love - Love"

  Scenario: Fifteen Love
    Given An empty game
    When PLAYER1 scores 1 point
    Then The score is "Fifteen - Love"

  Scenario: Adv1
    Given An empty game
    When PLAYER1 scores 4 point
    And PLAYER2 scores 3 point
    Then The score is "Advantage Player1"


  Scenario Template: Toate
    Given An empty game
    When PLAYER1 scores <player1Score> point
    And PLAYER2 scores <player2Score> point
    Then The score is "<expectedScoreString>"
    Examples:
      | player1Score | player2Score | expectedScoreString |
      | 0            | 0            | Love - Love         |
      | 0            | 1            | Love - Fifteen         |
      | 3            | 4            | Advantage Player2   |

