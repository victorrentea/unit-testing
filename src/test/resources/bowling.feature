Feature: Bowling

  Scenario Outline: Everything
    When The sequence of rolls is "<row>"
    Then The score is <score>
    And Means a "<meaning>"

    Examples:
      | row                                     | score | meaning |
      | 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 | 0     |         |
      | 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 | 1     |         |
      | 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 | 2     |         |
      | 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 | 3     |         |
      | 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 | 20    |         |
      #spare:
      | 1 9 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 | 12    | spare   |
      | 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 1 | 10    | spare   |
      #strike
      | 10 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0  | 14    | strike  |
      | 10 10 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0   | 33    | strike  |
      | 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10  | 10    | strike  |
      | 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10 2 1  | 16    | strike  |
#      | 10 10 10 2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 | 12    | strike   |
#      | 3 7 10 2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 | 12    | strike   |
