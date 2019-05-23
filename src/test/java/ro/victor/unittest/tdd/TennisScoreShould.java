package ro.victor.unittest.tdd;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TennisScoreShould {

 @Test
  public void loveLove() {
    assertEquals("Love-Love", displayScores(0,0));
  }

  @Test
  public void fifteenLove() {
    assertEquals("Fifteen-Love", displayScores(1,0));
  }

  @Test
  public void fifteenFifteen() {
    assertEquals("Fifteen-Fifteen", displayScores(1,1));
  }

  @Test
  public void loveFifteen() {
    assertEquals("Love-Fifteen", displayScores(0,1));
  }

  @Test
  public void thirtyLove() {
    assertEquals("Thirty-Love", displayScores(2,0));
  }

  @Test
  public void fortyLove() {
    assertEquals("Forty-Love", displayScores(3,0));
  }

  @Test
  public void deuce() {
    assertEquals("Deuce", displayScores(3,3));
  }

  @Test
  public void deuce4() {
    assertEquals("Deuce", displayScores(4,4));
  }

  @Test
  public void advantagePlayerOne() {
    assertEquals("Advantage Player One", displayScores(4, 3));
  }

  @Test
  public void advantagePlayerOne2() {
    assertEquals("Advantage Player One", displayScores(7,6));
  }

  @Test
  public void advantagePlayerTwo() {
    assertEquals("Advantage Player Two", displayScores(3,4));
  }

  @Test
  public void winPlayerOne() {
    assertEquals("Winner Player One", displayScores(8,6));
  }

  @Test
  public void winPlayerTwo() {
    assertEquals("Winner Player Two", displayScores(6,8));
  }

  @Test
  public void winPlayerOneToZero() {
    assertEquals("Winner Player One", displayScores(4,0));
  }

  private String displayScores(int player1, int player2) {
    TennisScore tennisScore = new TennisScore();
    setScorePlayerOne(player1, tennisScore);
    setScorePlayerTwo(player2, tennisScore);
    return tennisScore.score();
  }

  private void setScorePlayerOne(int points, TennisScore tennisScore) {
    for (int i = 0; i < points; i++) {
      tennisScore.addPointPlayerOne();
    }
  }

  private void setScorePlayerTwo(int points, TennisScore tennisScore) {
    for (int i = 0; i < points; i++) {
      tennisScore.addPointPlayerTwo();
    }
  }
}
