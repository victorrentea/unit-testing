package victor.testing.tennis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TennisScoreTest {
  TennisScore tennisScore = new TennisScore();

  @Test
  void loveLove() {
    String score = tennisScore.score();
    assertEquals("Love-Love", score);
  }

  @Test
  void loveFifteen() {
    setPlayerScore(2, 1);
    String score = tennisScore.score();
    assertEquals("Love-Fifteen", score);
  }

  @Test
  void fifteenLove() {
    setPlayerScore(1, 1);
    setPlayerScore(2, 0);
    String score = tennisScore.score();
    assertEquals("Fifteen-Love", score);
  }

  @Test
  void thirtyLove() {
    setPlayerScore(1, 2);
    setPlayerScore(2, 0);
    String score = tennisScore.score();
    assertEquals("Thirty-Love", score);
  }

  @Test
  void fortyLove() {
    setPlayerScore(1, 3);
    setPlayerScore(2, 0);
    String score = tennisScore.score();
    assertEquals("Forty-Love", score);
  }

  @Test
  void deuce() {
    setPlayerScore(1, 3);
    setPlayerScore(2, 3);
    String score = tennisScore.score();
    assertEquals("Deuce", score);
  }

  @Test
  void advantagePlayer1() {
    setPlayerScore(1, 7);
    setPlayerScore(2, 6);
    String score = tennisScore.score();
    assertEquals("Advantage Player1", score);
  }

  @Test
  void advantagePlayer2() {
    setPlayerScore(1, 3);
    setPlayerScore(2, 4);
    String score = tennisScore.score();
    assertEquals("Advantage Player2", score);
  }

  @Test
  void player1WonFast() {
    setPlayerScore(1, 4);
    setPlayerScore(2, 0);
    String score = tennisScore.score();
    assertEquals("Game won Player1", score);
  }

  @Test
  void player1WonAfterDeuce() {
    setPlayerScore(1, 7);
    setPlayerScore(2, 5);
    String score = tennisScore.score();
    assertEquals("Game won Player1", score);
  }

  @Test
  void player2Won() {
    setPlayerScore(1, 0);
    setPlayerScore(2, 4);
    String score = tennisScore.score();
    assertEquals("Game won Player2", score);
  }

  private void setPlayerScore(int playerNumber, int playerScore) {
    for (int i = 0; i < playerScore; i++) {
      tennisScore.addPoint(playerNumber);
    }
  }
}
