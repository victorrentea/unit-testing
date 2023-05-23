package victor.testing.tdd.tennis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {
  TennisGame tennisGame = new TennisGame();
  @Test
  void loveLove() {
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Love-Love");
  }
  @Test
  void fifteenLove() {
    tennisGame.addPointToFirstTeam();
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Fifteen-Love");
  }
  @Test
  void loveFifteen() {
    tennisGame.addPointToSecondTeam();
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Love-Fifteen");
  }
  @Test
  void fifteenFifteen() {
    tennisGame.addPointToFirstTeam();
    tennisGame.addPointToSecondTeam();
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Fifteen-Fifteen");
  }
  @Test
//  void thirtyFifteen() { // more complex than
  void thirtyLove() {
    tennisGame.addPointToFirstTeam();
    tennisGame.addPointToFirstTeam();
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Thirty-Love");
  }
//  @Test
//  void loveThirty() {
//    tennisGame.addPointToFirstTeam();
//    tennisGame.addPointToFirstTeam();
//    String actual = tennisGame.getScore();
//    assertThat(actual).isEqualTo("Love-Thirty");
//  }
  @Test
  void fortyLove() {
    tennisGame.addPointToFirstTeam();
    tennisGame.addPointToFirstTeam();
    tennisGame.addPointToFirstTeam();
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Forty-Love");
  }
  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
}
