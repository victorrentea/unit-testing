Feature: Tennis Game
#limbajul e "Gherkin", frameworkul Java e Cucumber
  Scenario: Love - Love
    Given A new tennis game
    Then Score is "Love - Love"

  Scenario: Fifteen - Love
    Given A new tennis game
    When Player1 scores
    Then Score is "Fifteen - Love"

  Scenario: Fifteen - Fifteen
    Given A new tennis game
    When Player1 scores
    And Player2 scores
    Then Score is "Fifteen - Fifteen"

  Scenario: Deuce
    Given A new tennis game
    When Player1 scores 3 points
    And Player2 scores 3 points
    Then Score is "Deuce"

  Scenario Outline: All Data Table
    Given A new tennis game
    When Player1 scores <player1Points> points
    And Player2 scores <player2Points> points
    Then Score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore     |
      | 0             | 0             | Love - Love         |
      | 1             | 0             | Fifteen - Love      |
      | 0             | 1             | Love - Fifteen      |
      | 1             | 1             | Fifteen - Fifteen   |
      | 2             | 0             | Thirty - Love       |
      | 3             | 0             | Forty - Love        |
      | 3             | 3             | Deuce             |
      | 4             | 4             | Deuce             |
      | 8             | 8             | Deuce             |
      | 4             | 3             | Advantage Player1 |
#      | 5             | 6             | Advantage Player2 |
#      | 8             | 6             | Game won Player1  |
#      | 0             | 4             | Game won Player2  |
#      | 1             | 4             | Game won Player2  |