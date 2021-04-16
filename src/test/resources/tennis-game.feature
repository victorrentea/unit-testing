Feature: Tennis Game

  Scenario Outline: Table -
    Given A new tennis game
    When Player "HOME" scores <player1Points> points
    And Player "AWAY" scores <player2Points> points
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
      | 5             | 6             | Advantage Player2 |
      | 8             | 6             | Game Won Player1  |
      | 0             | 4             | Game Won Player2  |
      | 1             | 4             | Game Won Player2  |