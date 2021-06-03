package victor.testing.tdd;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TennisScoreDisplayTest {

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
      String actual = translateScore(2, 0);

      assertThat(actual).isEqualTo("Thirty - Love");
   }
   @Test
   public void player1Scores3Point() {
      String actual = translateScore(3, 0);

      assertThat(actual).isEqualTo("Forty - Love");
   }
   @Test
   public void deuce() {
      String actual = translateScore(3, 3);

      assertThat(actual).isEqualTo("Deuce");
   }
   @Test
   public void deuce4() {
      String actual = translateScore(4, 4);

      assertThat(actual).isEqualTo("Deuce");
   }
   @Test
   public void player1Scores4Point() {
      assertThatThrownBy(() -> translateScore(4, 0))
         .isInstanceOf(IllegalArgumentException.class);
   }
   // overlapping:
//   @Test
//   public void player2Scores3Point() {
//      String actual = translateScore(0, 3);
//
//      assertThat(actual).isEqualTo("Love - Forty");
//   }

   private String translateScore(int player1Points, int player2Points) {
      addPoints(Players.ONE, player1Points);
      addPoints(Players.TWO, player2Points);
      return tennisScore.getDisplayScore();
   }

   private void addPoints(Players player, int points) {
      for (int i = 0; i < points; i++) {
         tennisScore.scorePoint(player);
      }
   }
//   private void addPointsToPlayer(Players player, int points) {
//      for (int i = 0; i < points; i++) {
//         tennisScore.scorePoint(player);
//      }
//   }
}
