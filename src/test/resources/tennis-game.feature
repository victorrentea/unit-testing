Feature: Tennis Score

  Scenario: Empty game
    Given An new game
    Then The score is "Love-Love"

  Scenario: First point
    Given An new game
    When Player "ONE" scores
    Then The score is "Fifteen-Love"

  Scenario Template: Toate
    Given An new game
    When Player "ONE" scores <player1Points>
    And Player "TWO" scores <player2Points>
    Then The score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore     |
      | 0             | 0             | Love-Love         |
      | 1             | 1             | Fifteen-Fifteen      |
      | 2             | 3             | Thirty-Forty      |
      | 5             | 5             | Deuce             |
      | 4             | 3             | Advantage Player1 |
      | 8             | 7             | Advantage Player1 |
      | 4             | 5             | Advantage Player2 |
#      | 10             | 2             | Advantage Player2 |
