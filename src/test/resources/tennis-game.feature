Feature: Tennis Game

  Scenario: Joc initial
    Given a new tennis game
    Then the score is "Love - Love"

  Scenario: Forty Love
    Given a new tennis game
    When player "ONE" scores 3 points
    Then the score is "Forty - Love"

  Scenario Template:
    Given a new tennis game
    When player "ONE" scores <points1> points
    And player "TWO" scores <points2> points
    Then the score is "<expectedScore>"

    Examples:
      | points1 | points2 | expectedScore  |
      | 0       | 1       | Love - Fifteen |
      | 0       | 2       | Love - Thirty  |
      | 6       | 6       | Deuce          |