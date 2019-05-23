package ro.victor.unittest.tdd;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TennisScoreParameterizedShould {

  private final int player1Score;
  private final int player2Score;
  private final String expectedScoreString;

  public TennisScoreParameterizedShould(int player1Score, int player2Score,
      String expectedScoreString) {
    this.player1Score = player1Score;
    this.player2Score = player2Score;
    this.expectedScoreString = expectedScoreString;
  }
  @Parameters(name = "For {0},{1} score is {2}")
  public static List<Object[]> oMetodaSeparata() {
    return Arrays.asList(
      new Object[]{0, 0, "Love-Love"},
      new Object[]{1, 0, "Fifteen-Love"}  ,
      new Object[]{2, 0, "Thirty-Love"}  ,
      new Object[]{3, 0, "Forty-Love"}  ,
      new Object[]{1, 1, "Fifteen-Fifteen"},
      new Object[]{4, 4, "Deuce"}
    );
  }

  @Test
  public void theTest() {
    assertEquals(expectedScoreString, displayScores(player1Score, player2Score));
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
