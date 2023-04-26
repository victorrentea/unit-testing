package victor.testing.tdd;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points
  // are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.

  @Test
  void zeroZero() {
    String score = new TennisScore().getScore();
    assertThat(score).isEqualTo("Love-Love");
  }

    // this test was leaking data -> testele de dupa vad datele ramase
    // - IN DB (daca e DB in-memory/in-docker) < nu la noi ca noi Mock repository
    // - stare (campuri) pe singletoane Spring -> @DirtiesContext (de evitate)
    // - campuri static
  @Test
  void unuZero() {
    TennisScore tennisScore = new TennisScore();
    tennisScore.winsPoint(Player.ONE);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Fifteen-Love");
  }
  @Test
  void doiZero() {
    TennisScore tennisScore = new TennisScore();
    tennisScore.winsPoint(Player.ONE);
    tennisScore.winsPoint(Player.ONE);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Thirty-Love");
  }
}
