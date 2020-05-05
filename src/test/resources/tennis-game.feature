@txn
Feature: Tennis Game

  Scenario: Joc nou
    Given An empty game
    Then The score is "Love-Love"

  Scenario: Batut mar
    Given An empty game
    When Player1 scores 4 point
    Then The score is "Game won Player1"

  Scenario Template: Toate
    Given An empty game
    When Player1 scores <player1Points> point
    And Player2 scores <player2Points> point
    Then The score is "<expectedScore>"

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
