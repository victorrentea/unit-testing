Feature: US15-Tennis Game

  Scenario: Empty Game
    Given A new Game
    Then Score is "Love - Love"


  Scenario: Love Fifteen
    Given A new Game
    When Player2 scores
    Then Score is "Love - Fifteen"

  Scenario: Love Fifteen
    Given A new Game
    When Player2 scores "2" points
    Then Score is "Love - Thirty"

  Scenario Outline: Tot
    Given A new Game
    When Player2 scores "<points2>" points
    And  Player1 scores "<points1>" points
    Then Score is "<expectedScore>"

    Examples:
      | points1 | points2 | expectedScore      |
      | 5       | 6       | Advantage Player 2 |
      | 5       | 7       | Win Player 2 |
      | 5       | 6       | Advantage Player 2 |
      | 5       | 6       | Advantage Player 2 |
      | 5       | 6       | Advantage Player 2 |
