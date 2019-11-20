Feature: Tennis Game

  Scenario: New Game
    Given An empty game
    Then Score is "Love Love"

  Scenario: New Game
    Given An empty game
    And Stuff
    When Player1 scores 1 point
    Then Score is "Fifteen Love"

  Scenario: New Game
    Given An empty game
    When Player1 scores 2 point
    Then Score is "Thirty Love"

  Scenario: New Game
    Given An empty game
    When Player1 scores 4 point
    When Player2 scores 4 point
    Then Score is "Deuce"

  Scenario Outline: All the scores
    Given An empty game
    When Player1 scores <player1> point
    When Player2 scores <player2> point
    Then Score is "<expectedScore>"

    Examples:
      | player1 | player2 | expectedScore     |
      | 0       | 0       | Love Love         |
      | 4       | 5       | Advantage Player2 |
      | 7       | 5       | Game Won Player1  |


