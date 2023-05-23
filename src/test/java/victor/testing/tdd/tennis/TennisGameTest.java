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
  @Test
  void fortyLove() {
    tennisGame.addPointToFirstTeam();
    tennisGame.addPointToFirstTeam();
    tennisGame.addPointToFirstTeam();
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Forty-Love");
  }

  // this new test went to green directly when TDDing.
  // what does that mean ?
  // 1) redundant (aka test overlapping) -> waste. tests have a maintenance cost
    // a) delete
    // b) keep if this is a translation from spec (PO)
  // 2) bug in a test -> fix the test
  @Test
  void loveThirty() {
    tennisGame.addPointToSecondTeam();
    tennisGame.addPointToSecondTeam();
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Love-Thirty");
  }
  // BREAKING NEWS: you can Test-Drive change requests:
  // write a failing test for stuff you did not change yet


  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.

  // -------

  // If at least three points have been scored by each player,
  // AND the scores are equal, the score is “Deuce”.
  @Test
  void deuce() {
    tennisGame.addPointToFirstTeam();
    tennisGame.addPointToFirstTeam();
    tennisGame.addPointToFirstTeam();
    tennisGame.addPointToSecondTeam();
    tennisGame.addPointToSecondTeam();
    tennisGame.addPointToSecondTeam();
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo("Deuce");
  }
}
