package victor.testing.tdd;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
   // tot ce pui pe campuri de instanta NU ramane intre teste caci JUnit intantiaza
   // cate o instanta pentru fiecare @Test
   private TennisScore tennisScore = new TennisScore();

   public TennisScoreTest() {
      System.out.println("De cate ori se instantiaza clasa de test?");
   }


   private void setPoints(int points1, int points2) {
      setPointsForPlayer(Player.ONE, points1);
      setPointsForPlayer(Player.TWO, points2);
   }

   // test-helper methods : inevitabile daca testezi MULT cod sau cu API ne prietenos
   private void setPointsForPlayer(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisScore.winsPoint(player);
      }
   }

   @Test
   void LoveLove_forNewGame() {
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Love-Love");
   }

   @Test
   void FifteenLove_whenFirstPlayerWinsAPoint() {
      // given = contextul (ce s-a intamplat in trecut) : mock, DB isnert, wiremock,
      setPoints(1, 0);

      // when = call de prod (codul testat)
      String actual = tennisScore.getScore();

      // then = verificarea efectelor
      assertThat(actual).isEqualTo("Fifteen-Love");
   }

   @Test
   void ThirtyLove_whenFirstPlayerWinsTwoPoints() {
      setPoints(2, 0);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Thirty-Love");
   }

   @Test
   void LoveFifteen_whenSecondPlayerWinsAPoint() {
      setPoints(0, 1);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Love-Fifteen");
   }

   @Test
      // A fost gata verde=> !!
      // 1) este corect (eg are asserts?) DA
      // 2) este overlapping : testeaza ceva ce deja e testat si implem?
      // o sa-l las pt ca e remarcabil
      // mai il lasam daca era test copy-paste din requirements.
   void FifteenFifteen_whenBothPlayersWinAPoint() {
      setPoints(1, 1);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen-Fifteen");
   }

   @Test
   void FortyLove_whenFirstPlayerWinsThreePoints() {
      setPoints(3, 0);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Forty-Love");
   }

   @Test
   void deuce_whenBothPlayersScore3Points() {
      setPoints(3, 3);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Deuce");
   }

   @Test
   void deuce_whenBothPlayersScore4Points() {
      setPoints(4, 4);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Deuce");
   }


   // If at least three points have been scored by each side and
   // a player has one more point than his opponent,
   // the score of the game is “Advantage” for the player in the lead.
   @Test
   void advantagePlayer1_whenPlayer1Scores4Points_andPlayer2Scores3Points() {
      setPoints(4, 3);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Advantage Player1");
   }

   @Test
   void advantagePlayer1_whenPlayer1Scores5Points_andPlayer2Scores4Points() {
      setPoints(5, 4);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Advantage Player1");
   }




}
