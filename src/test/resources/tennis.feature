@txn
Feature: Tennis Game

  Scenario: An empty game
    Given A new game
    Then Score is "Love - Love"

  Scenario: Fifteen Love
    Given A new game
    When Player1 scores one point
    Then Score is "Fifteen - Love"

  Scenario: Thirty Love
    Given A new game
    When Player1 scores 2 points
    Then Score is "Thirty - Love"


  Scenario Template: Toate
    Given A new game
    When Player1 scores <player1> points
    And Player2 scores <player2> points
    Then Score is "<expectedScore>"

    Examples:
      | player1 | player2 | expectedScore     |
      | 0       | 0       | Love - Love       |
      | 7       | 6       | Advantage Player1 |
      | 6       | 6       | Deuce             |
      | 2       | 3       | Thirty - Forty    |
