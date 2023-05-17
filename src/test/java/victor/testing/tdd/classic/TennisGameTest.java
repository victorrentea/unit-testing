package victor.testing.tdd.classic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {
  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
  @Test
  void initialGame() {
    String actual = new TennisGame().getScore();
    assertThat(actual).isEqualTo("Love:Love");
  }
  @Test
  void fifteen() {
    TennisGame game = new TennisGame();
    game.addPoint(Player.ONE);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Fifteen:Love");
  }
  @Test
  void thirty() {
    TennisGame game = new TennisGame();
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Thirty:Love");
  }
  @Test
  void forty() {
    TennisGame game = new TennisGame();
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Forty:Love");
  }
  @Test
  void player2Scores() {
    TennisGame game = new TennisGame();
    game.addPoint(Player.TWO);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Love:Fifteen");
  }

  // If at least three points have been scored by each player,
  // and the scores are equal, the score is “Deuce”.
  @Test
  void deuce() {
    TennisGame game = new TennisGame();
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    game.addPoint(Player.TWO);
    game.addPoint(Player.TWO);
    game.addPoint(Player.TWO);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Deuce");
  }
  @Test
  void deuce7() {
    TennisGame game = new TennisGame();
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    game.addPoint(Player.TWO);
    game.addPoint(Player.TWO);
    game.addPoint(Player.TWO);
    game.addPoint(Player.TWO);
    game.addPoint(Player.TWO);
    game.addPoint(Player.TWO);
    game.addPoint(Player.TWO);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Deuce");
  }
}
