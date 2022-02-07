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
      addPointsToPlayer2(2);
      String actual = tennisGame.getScore();

      assertThat(actual).isEqualTo("Love - Thirty");
   }

   @Test
   void returnsLoveForty_whenSecondPlayerScores3Points() {
      addPointsToPlayer2(3);
      String actual = tennisGame.getScore();

      assertThat(actual).isEqualTo("Love - Forty");
   }

   // building a 'testing framework' - is it good or bad.
   // - risk of bugs
   // + simpler shorter tests
   private void addPointsToPlayer2(int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.addPoint(Player.TWO);
      }
   }

   @Test
   void returnsFifteenLove_whenFirstPlayerScores1Point() {
      tennisGame.addPoint(Player.ONE);
      String actual = tennisGame.getScore();

      assertThat(actual).isEqualTo("Fifteen - Love");
   }

   // when to keep an overlapping test ? > in general they make tsts harder to maintain
   // > "Examples" in the requirements should be 100% translated to tests, even if they overlap
   // > It'a ramarkable example
   @Test
   void returnsThirtyLove_whenFirstPlayerScores2Point() {
      tennisGame.addPoint(Player.ONE);
      tennisGame.addPoint(Player.ONE);
      String actual = tennisGame.getScore();

      assertThat(actual).isEqualTo("Thirty - Love");
   }
}
