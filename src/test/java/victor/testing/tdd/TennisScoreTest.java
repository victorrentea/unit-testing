package victor.testing.tdd;
// String[]
//      let (score1, score2) = f(); ///"Love", "Love"
// Map<playerNo:int/enum, String>
// Player.getScore

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TennisScoreTest {
   @Test
   public void initial() {
      // init1
      // init2
      // init3
      // init4
      String actual = new TennisScore().getScore();
      assertThat(actual).isEqualTo("Love - Love");
   }

   // classic [Given]--When--Then style
//   public void whenPlayer2WinsAPoint_scoreIsLoveFifteen() {
   @Test
   public void loveFifteen_whenAwayPlayerWinsAPoint() {
      TennisScore tennisScore = new TennisScore();
      tennisScore.addPoint(Player.AWAY);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Love - Fifteen");
   }
   @Test
   public void loveFifteen_whenHomePlayerWinsAPoint() {
      TennisScore tennisScore = new TennisScore();
      tennisScore.addPoint(Player.HOME);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen - Love");
   }
   @Test
   public void loveFifteen_whenHomePlayerWins2Points() {
      TennisScore tennisScore = new TennisScore();
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
