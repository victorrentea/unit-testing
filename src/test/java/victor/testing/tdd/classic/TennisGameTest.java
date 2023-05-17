package victor.testing.tdd.classic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {
  TennisGame game = new TennisGame();
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
    game.addPoint(Player.ONE);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Fifteen:Love");
  }
  @Test
  void thirty() {
    game.addPoint(Player.ONE);
    game.addPoint(Player.ONE);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Thirty:Love");
  }
  @Test
  void forty() {
    addPoints(3, 0);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Forty:Love");
  }
  @Test
  void player2Scores() {
    game.addPoint(Player.TWO);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Love:Fifteen");
  }

  // If at least three points have been scored by each player,
  // and the scores are equal, the score is “Deuce”.
  @Test
  void deuce() {
    addPoints(3, 3);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Deuce");
  }
  @Test
  void deuce7() {
    addPoints(7, 7);
    String actual = game.getScore();
    assertThat(actual).isEqualTo("Deuce");
  }

  // Testing DSL = (Domain Specific Language)
  // mini-framework: test-helpere
  public void addPoints(int player1Points, int player2Points) {
    addPointsToPlayer(Player.ONE, player1Points);
    addPointsToPlayer(Player.TWO, player2Points);
  }
  public void addPointsToPlayer(Player player, int points) {
    for (int i = 0; i < points; i++) {
      game.addPoint(player);
    }

  }
}
