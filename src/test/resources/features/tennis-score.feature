Feature: Tennis Game

  Scenario: A new game
    Given A new tennis game
    Then Score is "Love:Love"

  Scenario: Player1 scores
    Given A new tennis game
    When Player1 scores
    Then Score is "Fifteen:Love"

  Scenario Template:
    Given A new tennis game
    When Player1 scores <p1p> points
    When Player2 scores <p2p> points
    Then Score is <expectedScore>
    Examples:
    | p1p | p2p|expectedScore|
    |2    |2   |Thirty:Thirty|


