package victor.testing.tdd;

import org.junit.jupiter.api.Test;
import victor.testing.tdd.TennisScore.Player;

import static org.assertj.core.api.Assertions.assertThat;

//@TestMethodOrder(Random.class)
public class TennisGameTest {

   private TennisScore tennisScore = new TennisScore();

   {
      System.out.println("O noua instanta din clasa de test");
   }

   // The running score of each game is described in a manner
   // peculiar to tennis: scores from zero to three points are
   // described as “Love”, “Fifteen”, “Thirty”, and “Forty”
   // respectively.
   @Test
   void loveLove_whenNewGame() {
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Love-Love");
   }
   @Test
   void fifteenLove_whenFirstPlayerWinsAPoint() {
      tennisScore.winsPoint(Player.ONE);// design de API rafinam semnaturile

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen-Love");
   }

   @Test
   void loveFifteen_whenSecondPlayerWinsAPoint() {
      tennisScore.winsPoint(Player.TWO);// design de API rafinam semnaturile

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Love-Fifteen");
   }

   @Test
   void fifteenFifteen_whenBothPlayersWinAPoint() {
      tennisScore.winsPoint(Player.ONE);
      tennisScore.winsPoint(Player.TWO);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen-Fifteen");
   }
//   void givenAEmtpyGame_whenPlayer2Scores2Points_returnsThirtyLove() {
//   void whenPlayer2Scores2Points_theReturnThirtyLove() {
   @Test
   void loveThirty_whenSecondPlayerWins2Points() {
      String actual = translateScore(0,2);

      assertThat(actual).isEqualTo("Love-Thirty");
   }

   public String translateScore(int player1Score, int player2Score) {
      setScoreForPlayer(player1Score, Player.ONE);
      setScoreForPlayer(player2Score, Player.TWO);
      return tennisScore.getScore();
   }

   private void setScoreForPlayer(int player2Score, Player two) {
      for (int i = 0; i < player2Score; i++) {
         tennisScore.winsPoint(two);
      }
   }
}
