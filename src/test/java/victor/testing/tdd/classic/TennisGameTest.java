package victor.testing.tdd.classic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {

   private TennisGame tennisGame = new TennisGame();
//   private SomeSearchCriteria criteria =  new...

   public TennisGameTest() {
      System.out.println("OUPS! I didn't know that for each @Test junit instantiate shte class again.");
   }

   // Scores from zero to three points are described as
   // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
   @Test
   void returnsLoveLove_whenNewGame() {
      String actual = tennisGame.getScore();

      assertThat(actual).isEqualTo("Love - Love");
   }

   @Test
   void returnsLoveFifteen_whenSecondPlayerScores1Point() {
      tennisGame.addPoint(Player.TWO);
      String actual = tennisGame.getScore();

      assertThat(actual).isEqualTo("Love - Fifteen");
   }

   @Test
   void returnsLoveThirty_whenSecondPlayerScores2Points() {
      tennisGame.addPoint(Player.TWO);
      tennisGame.addPoint(Player.TWO);
      String actual = tennisGame.getScore();

      assertThat(actual).isEqualTo("Love - Thirty");
   }

   @Test
   void returnsLoveForty_whenSecondPlayerScores3Points() {
      tennisGame.addPoint(Player.TWO);
      tennisGame.addPoint(Player.TWO);
      tennisGame.addPoint(Player.TWO);
      String actual = tennisGame.getScore();

      assertThat(actual).isEqualTo("Love - Forty");
   }

   @Test
   void returnsFifteenLove_whenFirstPlayerScores3Points() {
      tennisGame.addPoint(Player.ONE);
      String actual = tennisGame.getScore();

      assertThat(actual).isEqualTo("Fifteen - Love");
   }

}
