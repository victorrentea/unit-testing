package victor.testing.tdd;
// String[]
//      let (score1, score2) = f(); ///"Love", "Love"
// Map<playerNo:int/enum, String>
// Player.getScore

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TennisScoreTest {

   private TennisScore tennisScore = new TennisScore();


   public TennisScoreTest() {
      System.out.println("one new test instance per @Test");
   }


   @Test
   public void initial() {
      String actual = tennisScore.getScore();
      assertThat(actual).isEqualTo("Love - Love");
   }

   @Test
   public void loveFifteen_whenAwayPlayerWinsAPoint() {
      tennisScore.addPoint(Player.AWAY);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Love - Fifteen");
   }
   @Test
   public void loveFifteen_whenHomePlayerWinsAPoint() {
      tennisScore.addPoint(Player.HOME);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen - Love");
   }
   @Test
   public void loveFifteen_whenHomePlayerWins2Points() {
      tennisScore.addPoint(Player.HOME);
      tennisScore.addPoint(Player.HOME);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Thirty - Love");
   }
      //"Player1", new Player(), 2, "Away",

   // The running score of each game is described in a manner peculiar to tennis:
   // scores from zero to three points
   // are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
}
