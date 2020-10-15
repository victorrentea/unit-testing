Feature: Tennis Game

  Scenario: New Game
    Given A new tennis game
    Then Score is "Love:Love"


  Scenario: New Game
    Given A new tennis game
    When Player1 scores
    Then Score is "Fifteen:Love"

  Scenario: New Game
    Given A new tennis game
    When Player1 scores
    And Player2 scores
    Then Score is "Fifteen:Fifteen"

  Scenario: Deuce
    Given A new tennis game
    When Player1 scores 3 points
    And Player2 scores 3 points
    Then Score is "Deuce"

