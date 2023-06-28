package victor.testing.tdd.tennis;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
  TennisScore tennisScore = new TennisScore();
  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points are described as 
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
  @Test
  void newGame() {
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Love-Love");
  }

  @Test
  void fifteenLove_whenPlayer1Scores() {
    //TODO how to make first Player score 1 point
    tennisScore.player1Scored();
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Fifteen-Love");
  }
}
