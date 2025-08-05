Feature: Tennis Game

  Background: #@BeforeEach
    Given A new tennis game
#    And Shared state is cleaned

  Scenario: Love-Love
    Then Score is "Love-Love"

#  @timed
  @txn #opens a transaction for each scenario, rolling it back after it
  Scenario: Fifteen-Love
    When Player1 scores
    Then Score is "Fifteen-Love"

  Scenario: Fifteen-Fifteen
    When Player1 scores
    And Player2 scores
    Then Score is "Fifteen-Fifteen"

  Scenario: Deuce
    When Player1 scores 3 points
    And Player2 scores 3 points
    Then Score is "Deuce"

#  Scenario: Deuce
#    Given A new tennis game
#    When Player1 scores 7 points
#    Then Exception is raised

  Scenario Outline: All Data Table
    When Player1 scores <player1Points> points
    And Player2 scores <player2Points> points
    Then Score is "<expectedScore>"

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