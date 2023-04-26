package victor.testing.tdd;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {

  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points
  // are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.

  @Test
  void explore() {
    String score = TennisScore.getScore();
    assertThat(score).isEqualTo("Love-Love");
  }
}
