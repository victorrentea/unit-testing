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
    tennisGame.addPoint(Player.ONE);// enum;
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Fifteen-Love");
  }
  @Test
  void thirtyLove() {
    tennisGame.addPoint(Player.ONE);
    tennisGame.addPoint(Player.ONE);
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Thirty-Love");
  }
}
