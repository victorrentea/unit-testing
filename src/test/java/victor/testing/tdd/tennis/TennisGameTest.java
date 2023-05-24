package victor.testing.tdd.tennis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {
  private TennisGame tennisGame = new TennisGame();

  // The running score of each game is described in
  // a manner peculiar to tennis:
  // scores from zero to three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
  @Test
  void newGame() {
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Love-Love");
  }
  @Test
  void fifteenLove() {
    // given (setup of data, mocks, DB, MockServer..)
    tennisGame.addPoint(Player.ONE);// enum;

    // when (aka prod code call)
    String actual = tennisGame.getScore();

    // then (assert/verify)
    assertThat(actual).isEqualTo("Fifteen-Love");
  }
}
