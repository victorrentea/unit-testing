package victor.testing.tdd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import victor.testing.tdd.TennisGame.Player;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static victor.testing.tdd.TennisGame.Player.ONE;
import static victor.testing.tdd.TennisGame.Player.TWO;

@RunWith(Parameterized.class)
public class TennisGameParameterizedTest {
   private TennisGame tennisGame = new TennisGame();

   @Parameters(name = "When Player1 scores {0} and Player2 scores {1} then the score is {2}")
   public static List<Object[]> testData() {
      return Arrays.asList(
          new Object[]{0, 0, "Love - Love"},
          new Object[]{1, 0, "Fifteen - Love"},
          new Object[]{27, 26, "Advantage Player 1"});
   }
   private final int score1;
   private final int score2;
   private final String expectedScore;

   public TennisGameParameterizedTest(int score1, int score2, String expectedScore) {
      this.score1 = score1;
      this.score2 = score2;
      this.expectedScore = expectedScore;
   }

   private void setScore(int player1Points, int player2Points) {
      scorePoints(ONE, player1Points);
      scorePoints(TWO, player2Points);
   }

   // mini-test framework
   private void scorePoints(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.playerScores(player);
      }
   }
   private String translateScore(int player1Score, int player2Score) {
      setScore(player1Score, player2Score);
      return tennisGame.score();
   }



   @Test
   public void theOnlyTest() {
      String score = translateScore(score1, score2);
      assertEquals(expectedScore,score);
   }
}
