package victor.testing.tdd.tennis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {
  TennisGame tennisGame = new TennisGame();

  @BeforeEach
  final void before() {
    // only use this to:
    // - when..thenReturn
    // - pre-persist of data (eg countries) countryRepo.saveAll(allCountries);
    // var user  = userRepo.save(new User());
    // tennisGame = new TennisGame().setCreatedBy(user.id);// only initialize data in before if you need to fill some fields
    // from another inint
  }


  @Test
  void explore() {
    String score = tennisGame.getHumanReadableScore();
    assertThat(score).isEqualTo("Love-Love");
  }

  @Test
  void fifteenLove() {
    tennisGame.addPointToPlayer1();
    String score = tennisGame.getHumanReadableScore();
    assertThat(score).isEqualTo("Fifteen-Love");
  }

  @Test
  void loveFifteen() {
    tennisGame.addPointToPlayer2();
    String score = tennisGame.getHumanReadableScore();
    assertThat(score).isEqualTo("Love-Fifteen");
  }

  @Test
  void thirtyLove() {
    tennisGame.addPointToPlayer1();
    tennisGame.addPointToPlayer1();
    String score = tennisGame.getHumanReadableScore();
    assertThat(score).isEqualTo("Thirty-Love");
  }
  @Test
  void fortyLove() {
    tennisGame.addPointToPlayer1();
    tennisGame.addPointToPlayer1();
    tennisGame.addPointToPlayer1();
    String score = tennisGame.getHumanReadableScore();
    assertThat(score).isEqualTo("Forty-Love");
  }

  //The running score of each game is described in a manner peculiar
  // to tennis: scores from zero to three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.



  // If at least three points have been scored by each player,
  // and the scores are equal, the score is “Deuce”.

  @Test
  void scoreIsDeuce_whenBothPlayersScored3Points() {
    addPointsToPlayer(3, 1);
    addPointsToPlayer(3, 2);
    String score = tennisGame.getHumanReadableScore();
    assertThat(score).isEqualTo("Deuce");
  }
  @Test
  void scoreIsDeuce_whenBothPlayersScored10Points() {
//    tennisGame.addPointToPlayer1(10); // #2 DONT if you do it JUST FOR TESTS
        // it makes no sense in a real game to win 10 points at once

//    test-helper (Testing DSL) // #1
    addPointsToPlayer(10, 1);
    addPointsToPlayer(10, 2);
    String score = tennisGame.getHumanReadableScore();
    assertThat(score).isEqualTo("Deuce");
  }

  private void addPointsToPlayer(int points, int player) {
    for (int i = 0; i < points; i++) {
      if (player == 1) {
        tennisGame.addPointToPlayer1();
      } else {
        tennisGame.addPointToPlayer2();
      }
    }
  }
}
