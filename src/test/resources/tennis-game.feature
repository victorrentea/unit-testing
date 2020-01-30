@txn
Feature: Tennis Score

  Scenario: Empty Game - Dragoste
    Given An empty game
    Then The score is "Love-Love"

  Scenario: Fifteen Love
    Given An empty game
    When Player1 scores
    Then The score is "Fifteen-Love"

  Scenario: Fifteen Love
    Given An empty game
    When Player1 scores
    And Player2 scores
    Then The score is "Fifteen-Fifteen"

  Scenario: Deuce
    Given An empty game
    When Player1 scores 3 points
    And Player2 scores 3 points
    Then The score is "Deuce"


  Scenario Outline: Toate (businessul are o lacrima in coltul ochiului)
    Given An empty game
    When Player1 scores <pointsPlayer1> points
    And Player2 scores <pointsPlayer2> points
    Then The score is "<expectedScore>"

    Examples:
      | pointsPlayer1 | pointsPlayer2 | expectedScore        |
      | 0             | 0             | Love-Love            |
      | 0             | 1             | Love-Fifteen            |
      | 6             | 4             | Game won Player1 |
      | 7             | 8             | Advantage Player2   |