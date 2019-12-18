Feature: Job Search

  Scenario: By name
    Given A job exists with name "Jr Architect"
    When I search by name "Jr Architect"
    Then I find the job