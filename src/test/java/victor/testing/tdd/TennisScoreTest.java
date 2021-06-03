package victor.testing.tdd;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TennisScoreTest {

   private TennisScore tennisScore = new TennisScore();

   @Test
   public void initialScore() {
      String actual = tennisScore.getDisplayScore();
      assertThat(actual).isEqualTo("Love - Love");
   }
   // “Love”, “Fifteen”, “Thirty”, and “Forty”
   @Test
   public void player1Scores1Point() {
      tennisScore.scorePoint(Players.ONE);
      String actual = tennisScore.getDisplayScore();

      assertThat(actual).isEqualTo("Fifteen - Love");
   }
   @Test
   public void player2Scores1Point() {
      tennisScore.scorePoint(Players.TWO);
      String actual = tennisScore.getDisplayScore();

      assertThat(actual).isEqualTo("Love - Fifteen");
   }

   // e un pic reduntant dar util pentru ca NE imaginam e un caz rermarcabil de biz
   @Test
   public void bothPlayersScore1Point() {
      tennisScore.scorePoint(Players.ONE);
      tennisScore.scorePoint(Players.TWO);
      String actual = tennisScore.getDisplayScore();

      assertThat(actual).isEqualTo("Fifteen - Fifteen");
   }

   @Test
   public void player1Scores2Point() {
      tennisScore.scorePoint(Players.ONE);
      tennisScore.scorePoint(Players.ONE);
      String actual = tennisScore.getDisplayScore();

      assertThat(actual).isEqualTo("Thirty - Love");
   }
}
