package victor.testing.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TennisScoreTest {
  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points are described as “Love”, “Fifteen”, “Thirty”,
  // and “Forty” respectively.
  @Test
  void newGame() {
    String actual = TennisScore.getScore();

    assertEquals("Love - Love", actual);
  }

  @Test
  void scoreIsFifteenLove_whenPlayer1Scored1Point() {
    TennisScore.addPointToPlayer1();

    String actual = TennisScore.getScore();

    assertEquals("Fifteen - Love", actual);
  }
}
