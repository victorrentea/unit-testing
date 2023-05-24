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

  @Test
  void loveFifteen() {
    tennisGame.addPoint(Player.TWO);
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Love-Fifteen");
  }

  @Test
  void fortyLove() {// the simplest test to explore what you want
    tennisGame.addPoint(Player.ONE);
    tennisGame.addPoint(Player.ONE);
    tennisGame.addPoint(Player.ONE);
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Forty-Love");
  }

  //If at least >= three points have been scored by each player,
  // AND the scores are equal==, the score is “Deuce”.
  @Test
  void deuce() {// the simplest test to explore what you want
    addPoints(Player.ONE, 3);
    addPoints(Player.TWO, 3);
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Deuce");
  }
  @Test
  void deuce4() {// the simplest test to explore what you want
//    tennisGame.addPoint(Player.ONE, 4); // NO bvecause it's not a business need, it's just a test need
    // Tests only play with prod via the public API, respecting encapsulation
    addPoints(Player.ONE, 4);
    addPoints(Player.TWO, 4);
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Deuce");
  }

  private void addPoints(Player onplayer, int points) {
    for (int i = 0; i < points; i++) {
      tennisGame.addPoint(onplayer);
    }
  }
}
