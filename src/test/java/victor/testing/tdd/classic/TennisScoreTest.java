package victor.testing.tdd.classic;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.tdd.classic.TennisScore.Player;
import victor.testing.tools.ReplaceCamelCase;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(ReplaceCamelCase.class)
public class TennisScoreTest {

   private TennisScore tennisScore = new TennisScore();

   public TennisScoreTest() {
      System.out.println("NEW INSTACE");
   }

   // The running score of each game is described in a manner peculiar to tennis:
   // scores from zero to three points are described as “Love”, “Fifteen”, “Thirty”,
   // and “Forty” respectively.You need only report the score for the current game.
   @Test
//   @DisplayName("out of sync, just like implementation comments")
   void givenNewGame_returnsLoveLove() {
      String actual = tennisScore.currentScore(); // focus on the public API

      assertThat(actual).isEqualTo("Love-Love");// TDD: questions earlier
      // take-away: when some biz hands you the requirements, you should immediately write empty tescases
   }

   @Test
   void givenPlayer1Scores_returnsFifteenLove() {
      tennisScore.winsPointByPlayer(Player.ONE);

      String actual = tennisScore.currentScore();

      assertThat(actual).isEqualTo("Fifteen-Love");
   }
   @Test
   void givenBothPlayersScores1Point_returnsFifteenFifteen() {
      tennisScore.winsPointByPlayer(Player.ONE);
      tennisScore.winsPointByPlayer(Player.TWO);

      String actual = tennisScore.currentScore();

      assertThat(actual).isEqualTo("Fifteen-Fifteen");
   }

   @Test
   void givenPlayer2Scores_returnsLoveFifteen() {
      // given MUST-have for your first 100 tests or when mocking a lot
      tennisScore.winsPointByPlayer(Player.TWO);

      // when <<   if the given is short, only use empty lines between the section
      String actual = tennisScore.currentScore();

      // then
      assertThat(actual).isEqualTo("Love-Fifteen");
   }

   @Test
   void givenPlayer1Scores2Points_returnsThirtyLove() {
//      tennisScore.winsPointByPlayer1(2); // never useful in prod use cases
      // there is No BIZ NEED to give 2 poitns at once. In prod it makes no sense
      tennisScore.winsPointByPlayer(Player.ONE);
      tennisScore.winsPointByPlayer(Player.ONE);

      String actual = tennisScore.currentScore();

      assertThat(actual).isEqualTo("Thirty-Love");
   }

   @Test
   void givenPlayer1Scores3Points_returnsFortyLove() {
      // idea : for() in  tests as a helper method. << the cost of helper methods (mini testing frameworks) :
      //testing framework ROI is high if the logic you are testing requires maaany tests 20+

      // because the test names don't help much, >>> ParamterizedTests (excel-driven tests)
      playerWinsPoints(Player.ONE, 3);

      String actual = tennisScore.currentScore();

      assertThat(actual).isEqualTo("Forty-Love");
   }
   @Test
   void deuce2() {
      playerWinsPoints(Player.ONE, 2);
      playerWinsPoints(Player.TWO, 2);

      String actual = tennisScore.currentScore();

      assertThat(actual).isEqualTo("Thirty-Thirty");
   }
   @Test
   void deuce7() {
      playerWinsPoints(Player.ONE, 7);
      playerWinsPoints(Player.TWO, 7);

      String actual = tennisScore.currentScore();

      assertThat(actual).isEqualTo("Thirty-Thirty");
   }
   @Test
   void deuce3() {
      playerWinsPoints(Player.ONE, 3);
      playerWinsPoints(Player.TWO, 3);

      String actual = tennisScore.currentScore();

      assertThat(actual).isEqualTo("Deuce");
   }
   @Test
   void deuce4() {
      playerWinsPoints(Player.ONE, 4);
      playerWinsPoints(Player.TWO, 4);

      String actual = tennisScore.currentScore();

      assertThat(actual).isEqualTo("Deuce");
   }

   private void playerWinsPoints(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisScore.winsPointByPlayer(player);
      }
   }
}