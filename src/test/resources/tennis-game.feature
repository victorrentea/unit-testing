Feature: Tennis Game

  Scenario: Joc initial
    Given a new tennis game
    Then the score is "Love - Love"

  Scenario: Joc initial
    Given a new tennis game
    When Player "ONE" marks 3 points
    And Player "TWO" marks 3 points
    Then the score is "Deuce"

  Scenario Outline: Toate cazurile !
    Given a new tennis game
    When Player "ONE" marks <player1Points> points
    And Player "TWO" marks <player2Points> points
    Then the score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore    |
      | 1             | 2             | Fifteen - Thirty |
      | 2             | 2             | Thirty - Thirty  |

