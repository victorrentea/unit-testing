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
    addPointsToPlayer(2, 1);
    String score = tennisScore.score();
    assertEquals("Love-Fifteen", score);
  }

  @Test
  void fifteenLove() {
    addPointsToPlayer(1, 1);
    addPointsToPlayer(2, 0);
    String score = tennisScore.score();
    assertEquals("Fifteen-Love", score);
  }

  @Test
  void thirtyLove() {
    addPointsToPlayer(1, 2);
    addPointsToPlayer(2, 0);
    String score = tennisScore.score();
    assertEquals("Thirty-Love", score);
  }

  @Test
  void fortyLove() {
    addPointsToPlayer(1, 3);
    addPointsToPlayer(2, 0);
    String score = tennisScore.score();
    assertEquals("Forty-Love", score);
  }

  @Test
  void deuce() {
    addPointsToPlayer(1, 3);
    addPointsToPlayer(2, 3);
    String score = tennisScore.score();
    assertEquals("Deuce", score);
  }

  @Test
  void advantagePlayer1() {
    addPointsToPlayer(1, 7);
    addPointsToPlayer(2, 6);
    String score = tennisScore.score();
    assertEquals("Advantage Player1", score);
  }

  @Test
  void advantagePlayer2() {
    addPointsToPlayer(1, 3);
    addPointsToPlayer(2, 4);
    String score = tennisScore.score();
    assertEquals("Advantage Player2", score);
  }

  @Test
  void player1WonFast() {
    addPointsToPlayer(1, 4);
    addPointsToPlayer(2, 0);
    String score = tennisScore.score();
    assertEquals("Game won Player1", score);
  }

  @Test
  void player1WonAfterDeuce() {
    addPointsToPlayer(1, 7);
    addPointsToPlayer(2, 5);
    String score = tennisScore.score();
    assertEquals("Game won Player1", score);
  }

  @Test
  void player2Won() {
    addPointsToPlayer(1, 0);
    addPointsToPlayer(2, 4);
    String score = tennisScore.score();
    assertEquals("Game won Player2", score);
  }

  private void addPointsToPlayer(int playerNumber, int playerScore) {
    for (int i = 0; i < playerScore; i++) {
      tennisScore.addPoint(playerNumber);
    }
  }
}
