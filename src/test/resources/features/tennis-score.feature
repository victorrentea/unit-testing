# Gherkin language
Feature: Tennis Score

  Background: #~ beforeEach
#    Given A new game between "John" and "Jane" #paramterize the relevant data.
    Given A new game

  @txn
  Scenario: New Game
    Then The score is "Love - All"

  Scenario: Player 1 wins a point
    When Player 1 scores a point
    Then The score is "Fifteen - Love"

  Scenario: 2-3
    When Player 1 scores 2 points
    And Player 2 scores 3 points
    Then The score is "Thirty - Forty"

  #like a @Paramtereized test:
  Scenario Outline: All tests
    When Player 1 scores <p1> points
    And Player 2 scores <p2> points
    Then The score is "<score>"

    Examples:
      | p1 | p2 | score            |
      | 0  | 0  | Love - All       |
      | 1  | 0  | Fifteen - Love   |
      | 2  | 3  | Thirty - Forty   |
      | 3  | 3  | Deuce            |
#      | 4  | 0  | Game won by John |