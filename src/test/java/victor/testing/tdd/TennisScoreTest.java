package victor.testing.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TennisScoreTest {
  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
  @Test
  void newGame() {
    String actual = new TennisScore().getScore();

    assertEquals("Love - Love", actual);
  }

  @Test
  void scoreIsFifteenLove_whenPlayer1Scored1Point() {
    TennisScore tennisScore = new TennisScore();
    tennisScore.addPointToPlayer1();

    String actual = tennisScore.getScore();

    assertEquals("Fifteen - Love", actual);
  }

  @Test
  void scoreIsThirtyLove_whenPlayer1Scored2Point() {
    TennisScore tennisScore = new TennisScore();
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();

    String actual = tennisScore.getScore();

    assertEquals("Thirty - Love", actual);
  }
  @Test
  void scoreIsFortyLove_whenPlayer1Scored3Point() {
    TennisScore tennisScore = new TennisScore();
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();

    String actual = tennisScore.getScore();

    assertEquals("Forty - Love", actual);
  }
}
