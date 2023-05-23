Feature: Tennis Game

  Background:  #@BeforeEach
    Given A new tennis game

  Scenario: Initial Game
    Then Score is "Love-Love"

  Scenario: Fifteen-Love
    When First Team scores
    Then Score is "Fifteen-Love"

  Scenario: Fifteen-Fifteen
    When First Team scores
    And Second Team scores
    Then Score is "Fifteen-Fifteen"

  Scenario: The longest game in the world
    When First Team scores 43 points
    And Second Team scores 44 points
    Then Score is "Advantage"

  Scenario: Deuce
    When First Team scores 3 points
    And Second Team scores 3 points
    Then Score is "Deuce"

  Scenario Outline: All Data Table
    When First Team scores <player1Points> points
    And Second Team scores <player2Points> points
    Then Score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore    |
      | 0             | 0             | Love-Love        |
      | 1             | 0             | Fifteen-Love     |
      | 0             | 1             | Love-Fifteen     |
      | 1             | 1             | Fifteen-Fifteen  |
      | 2             | 0             | Thirty-Love      |
      | 3             | 0             | Forty-Love       |
      | 3             | 3             | Deuce            |
      | 4             | 4             | Deuce            |
      | 8             | 8             | Deuce            |
      | 4             | 3             | Advantage        |
      | 5             | 6             | Advantage        |
#      | 8             | 6             | Game won Player1 |
#      | 0             | 4             | Game won Player2 |
#      | 1             | 4             | Game won Player2 |