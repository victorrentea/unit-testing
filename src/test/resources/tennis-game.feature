Feature: Tennis Game

  Scenario: Joc nou
    Given A new game
    Then The score is "Love - Love"

  Scenario: 1 punct pt jucatorul 1
    Given A new game
    When Player1 scored a point
    Then The score is "Fifteen - Love"

  Scenario: 1 punct pt jucatorul 2
    Given A new game
    When Player2 scored a point
    Then The score is "Love - Fifteen"

  Scenario: Batut mar
    Given A new game
    When Player1 scored 4 points
    Then The score is "Game Won Player 1"

  Scenario Outline: Toate cazurile
    Given A new game
    When Player1 scored <player1Points> points
    And Player2 scored <player2Points> points
    Then The score is "<expectedScore>"

    Examples:
      | player1Points | player2Points | expectedScore      |
      | 0             | 0             | Love - Love        |
      | 0             | 1             | Love - Fifteen     |
      | 9             | 7             | Game Won Player 1  |
      | 9             | 8             | Advantage Player 1 |

