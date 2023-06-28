package victor.testing.tdd.tennis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
  TennisScore tennisScore = new TennisScore();

  @Test
  void newGame() {
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Love-Love");
  }

  @Test
  void fifteenLove_whenPlayer1Scores() {
    tennisScore.player1Scored();
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Fifteen-Love");
  }

  @Test
  void thirtyLove_whenPlayer1Scores2Points() {
    tennisScore.player1Scored();
    tennisScore.player1Scored();
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Thirty-Love");
  }

  @Test
  void fortyLove_whenPlayer1Scores3Points() {
    tennisScore.player1Scored();
    tennisScore.player1Scored();
    tennisScore.player1Scored();
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Forty-Love");
  }

  @Test
  void loveFifteen_whenPlayer2Scores() {
    tennisScore.player2Scored();
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Love-Fifteen");
  }

//  void scoreCannotIncreasePats() {
//  }
  // is it my respon to reject invalid input? NO/idn/YES -> perhaps add more guards to the prod code, with test ahead of them

  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.


  // If at least three points have been scored by each player,
  // and the scores are equal, the score is “Deuce”.
  @Test
  void deuce_whenBothPlayersScore3Points() {
    player1ScoredPoints(3);
    player2ScoredPoints(3);

    String score = tennisScore.getScore();

    assertThat(score).isEqualTo("Deuce");
  }
  @Test
  void deuce_whenBothPlayersScore5Points() {
    // bothPlayersScore2Points() // B

//    player1ScoresPoints(5); // A test helper mini-framework
    player1ScoredPoints(5);

//    tennisScore.player2ScoredPoints(5);// C change the prod API // NOT HERE
    // this change is DONE SOLELY for TeSTS, this arg is USELESS in prod code
    player2ScoredPoints(5);

    String score = tennisScore.getScore();

    assertThat(score).isEqualTo("Deuce");
  }

  private void player1ScoredPoints(int points) {
    for (int i = 0; i < points; i++) {
      tennisScore.player1Scored();
    }
  }

  private void player2ScoredPoints(int points) {
    for (int i = 0; i < points; i++) {
      tennisScore.player2Scored();
    }
  }
}
