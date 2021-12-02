Feature: Tennis Game

  Scenario: New Game
    Given A new tennis game
    Then Score is "Love:Love"


  Scenario: Fifteen Love
    Given A new tennis game
    When Player "ONE" scores
    Then Score is "Fifteen:Love"

  Scenario: New Game 2
    Given A new tennis game
    When Player "ONE" scores
    And Player "TWO" scores
    Then Score is "Fifteen:Fifteen"

  Scenario: Deuce
    Given A new tennis game
    When Player "ONE" scores 3 points
    And Player "TWO" scores 3 points
#    And Customer is "customer1.json"
#    or
#    And Customer name is "JOhn"
#    And Customer age is 9
    Then Score is "Deuce"

  Scenario Template: X
    Given A new tennis game
    When Player "ONE" scores <player1Points> points
    And Player "TWO" scores <player2Points> points
    Then Score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore     |
      | 0             | 0             | Love:Love         |
      | 1             | 0             | Fifteen:Love      |
      | 0             | 1             | Love:Fifteen      |
      | 1             | 1             | Fifteen:Fifteen   |
      | 2             | 0             | Thirty:Love       |
      | 3             | 0             | Forty:Love        |
      | 3             | 3             | Deuce             |
      | 4             | 4             | Deuce             |
      | 8             | 8             | Deuce             |
      | 4             | 3             | Advantage Player1 |
#      | 5             | 6             | Advantage Player2 |
#      | 8             | 6             | Game won Player1  |
#      | 0             | 4             | Game won Player2  |
#      | 1             | 4             | Game won Player2  |