package ro.victor.unittest.tdd;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ro.victor.unittest.tdd.TennisGame.Player;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParameterizedTests {
   @Parameters(name = "Score for ({0},{1}) = {2}")
   public static List<Object> getTestData() {
//      File folder;
//      for (File file : folder.listFiles()) {
//         // determine output file
//         testData.add(new Object[]{inFile, outFile});
//      }
      List<Object> testData = Arrays.asList(
          new Object[]{0, 0, "Love - Love"},
          new Object[]{1, 0, "Fifteen - Love"},
          new Object[]{0, 2, "Love - Thirty"}
      );
      return testData;
   }

   private final int points1;
   private final int points2;
   private final String expectedScore;

   public ParameterizedTests(int points1, int points2, String expectedScore) {
      this.points1 = points1;
      this.points2 = points2;
      this.expectedScore = expectedScore;
   }

   private String getScoreString(int player1Points, int player2Points) {
      TennisGame tennisGame = new TennisGame();
      markPointsForPlayer(Player.ONE, player1Points, tennisGame);
      markPointsForPlayer(Player.TWO, player2Points, tennisGame);
      return tennisGame.score();
   }

   private void markPointsForPlayer(Player player, int points, TennisGame tennisGame) {
      for (int i = 0; i < points; i++) {
         tennisGame.markPointFor(player);
      }
   }

   @Test
   public void theTest() {
      assertEquals(expectedScore, getScoreString(points1,points2));
   }
}
