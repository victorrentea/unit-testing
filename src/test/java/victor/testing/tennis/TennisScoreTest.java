package victor.testing.tennis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// product of a TDD session on Tennis Kata🥋: https://kata-log.rocks/tennis-kata
class TennisScoreTest {
  TennisScore tennisScore = new TennisScore();

  @Test
  void loveLove() {
    setPlayerScore(1, 0);
    setPlayerScore(2, 0);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Love-Love");
  }

  @Test
  void loveFifteen() {
    setPlayerScore(1, 0);
    setPlayerScore(2, 1);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Love-Fifteen");
  }

  @Test
  void fifteenLove() {
    setPlayerScore(1, 1);
    setPlayerScore(2, 0);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Fifteen-Love");
  }

  @Test
  void thirtyLove() {
    setPlayerScore(1, 2);
    setPlayerScore(2, 0);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Thirty-Love");
  }

  @Test
  void fortyLove() {
    setPlayerScore(1, 3);
    setPlayerScore(2, 0);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Forty-Love");
  }

  @Test
  void deuce() {
    setPlayerScore(1, 3);
    setPlayerScore(2, 3);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Deuce");
  }

  @Test
  void advantagePlayer1() {
    setPlayerScore(1, 7);
    setPlayerScore(2, 6);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Advantage Player1");
  }

  @Test
  void advantagePlayer2() {
    setPlayerScore(1, 3);
    setPlayerScore(2, 4);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Advantage Player2");
  }

  @Test
  void player1WonFast() {
    setPlayerScore(1, 4);
    setPlayerScore(2, 0);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Game won Player1");
  }

  @Test
  void player1WonAfterDeuce() {
    setPlayerScore(1, 7);
    setPlayerScore(2, 5);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Game won Player1");
  }

  @Test
  void player2Won() {
    setPlayerScore(1, 0);
    setPlayerScore(2, 4);
    String score = tennisScore.score();
    assertThat(score).isEqualTo("Game won Player2");
  }

  private void setPlayerScore(int playerNumber, int playerScore) {
    for (int i = 0; i < playerScore; i++) {
      tennisScore.addPoint(playerNumber);
    }
  }
}
