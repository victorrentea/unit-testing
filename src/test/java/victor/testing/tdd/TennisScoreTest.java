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

   @Test
   void LoveLove_forNewGame() {
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Love-Love");
   }

   @Test
   void FifteenLove_whenFirstPlayerWinsAPoint() {
      // given = contextul (ce s-a intamplat in trecut) : mock, DB isnert, wiremock,
      tennisScore.winsPoint(Player.ONE);

      // when = call de prod (codul testat)
      String actual = tennisScore.getScore();

      // then = verificarea efectelor
      assertThat(actual).isEqualTo("Fifteen-Love");
   }

   @Test
   void ThirtyLove_whenFirstPlayerWinsTwoPoints() {
      tennisScore.winsPoint(Player.ONE);
      tennisScore.winsPoint(Player.ONE);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Thirty-Love");
   }

   @Test
   void LoveFifteen_whenSecondPlayerWinsAPoint() {
      tennisScore.winsPoint(Player.TWO);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Love-Fifteen");
   }
   @Test // A fost gata verde=> !!
   // 1) este corect (eg are asserts?) DA
   // 2) este overlapping : testeaza ceva ce deja e testat si implem?
   // o sa-l las pt ca e remarcabil
   // mai il lasam daca era test copy-paste din requirements.
   void FifteenFifteen_whenBothPlayersWinAPoint() {
      tennisScore.winsPoint(Player.ONE);
      tennisScore.winsPoint(Player.TWO);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen-Fifteen");
   }

   @Test
   void FortyLove_whenFirstPlayerWinsThreePoints() {
      tennisScore.winsPoint(Player.ONE);
      tennisScore.winsPoint(Player.ONE);
      tennisScore.winsPoint(Player.ONE);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Forty-Love");
   }

   @Test
   void deuce_whenBothPlayersScore3Points() {
      tennisScore.winsPoint(Player.ONE);
      tennisScore.winsPoint(Player.ONE);
      tennisScore.winsPoint(Player.ONE);
      tennisScore.winsPoint(Player.TWO);
      tennisScore.winsPoint(Player.TWO);
      tennisScore.winsPoint(Player.TWO);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Deuce");
   }
   @Test
   void deuce_whenBothPlayersScore4Points() {
      // O GRESEALA AICI PT CA NU ARE SENS O ASA FUNCTIE IN COD DEPROD
      // INCALCA INCAPSULAREA
      tennisScore.setPoints(Player.ONE, 4);
      tennisScore.setPoints(Player.TWO, 4);

//      tennisScore.winsPoint(Player.ONE);
//      tennisScore.winsPoint(Player.ONE);
//      tennisScore.winsPoint(Player.ONE);
//      tennisScore.winsPoint(Player.ONE);
//      tennisScore.winsPoint(Player.TWO);
//      tennisScore.winsPoint(Player.TWO);
//      tennisScore.winsPoint(Player.TWO);
//      tennisScore.winsPoint(Player.TWO);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Deuce");
   }

}
