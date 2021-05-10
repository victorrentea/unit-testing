package victor.testing.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TennisGameParameterizedTest {

   private TennisGame tennisGame = new TennisGame();

   public TennisGameParameterizedTest() {
      System.out.println("HOW MANY");
   }

   @ParameterizedTest(name = "Player ONE scores {0} and Player TWO scores {1} then score is {2}")
   @MethodSource
   public void theTest(int score1, int score2, String expectedScoreStr) {
      setScore(score1, score2);
      String score = tennisGame.getScore();
      assertEquals(expectedScoreStr, score);
   }

   public static List<Object[]> theTest() {
      return asList(
          new Object[] {1,1,"Fifteen - Fifteen"},
          new Object[] {4,4,"Deuce"}
          );

   }

   private void setScore(int player1Score, int player2Score) {
      addPointsToParty(player1Score, TennisParty.ONE);
      addPointsToParty(player2Score, TennisParty.TWO);
   }

   private void addPointsToParty(int points, TennisParty party) {
      for (int i = 0; i < points; i++) {
         tennisGame.addPoint(party);
      }
   }
}
