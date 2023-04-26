package victor.testing.tdd;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(OrderAnnotation.class)
public class TennisScoreTest {
  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points
  // are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.

  @Test
  @Order(1)
  void zeroZero() {
    String score = TennisScore.getScore();
    assertThat(score).isEqualTo("Love-Love");
  }

  @Test
  @Order(2)
  void unuZero() {
    TennisScore.winsPoint(Player.ONE);
    String score = TennisScore.getScore();
    assertThat(score).isEqualTo("Fifteen-Love");
  }
}
