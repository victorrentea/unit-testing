package victor.testing.tdd.outsidein;

import org.junit.jupiter.api.Test;
import victor.testing.tdd.outsidein.TennisGame.Player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TennisGameTest {

   // The running score of each game is described in a manner peculiar to tennis:
   // scores from zero to three points are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
   private TennisGame tennisGame = new TennisGame();


   //@BeforeClass @BeforeAll // instead, create a custom @Rule
   // infant testing framework
   private void addPointsToPlayer(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.playerWonPoint(player);
      }
   }

   // custom Domain Specific Language (DSL)
   private void setPoints(int points1, int points2) {
      addPointsToPlayer(Player.ONE, points1);
      addPointsToPlayer(Player.TWO, points2);
   }

   private String translateScore(int pointsPlayer1, int pointsPlayer2) {
      setPoints(pointsPlayer1, pointsPlayer2);

      return tennisGame.getScore();
   }

   @Test
   void loveLove_givenANewGame() {
      String actual = translateScore(0, 0);

      assertThat(actual).isEqualTo("Love:Love"); // 💗 AssertJ library
   }
   @Test
   void fifteenLove_givenPlayer1Scores() {
      // given
      String actual = translateScore(1, 0);

      // then
      assertThat(actual).isEqualTo("Fifteen:Love");
   }

   @Test
   void loveFifteen_givenPlayer2Scores() {
      String actual = translateScore(0, 1);

      assertThat(actual).isEqualTo("Love:Fifteen");
   }

   @Test
   void throwsForNullPlayer() {  // must have : for utility/library code
      assertThatThrownBy(() -> tennisGame.playerWonPoint(null))
          .hasMessage("Player must not be null")
          .isInstanceOf(NullPointerException.class);
   }

   @Test
   void thirtyLove() {
      String actual = translateScore(2, 0);

      assertThat(actual).isEqualTo("Thirty:Love");
   }
   @Test
   void fortyLove() {
      String actual = translateScore(3, 0);

      assertThat(actual).isEqualTo("Forty:Love");
   }
   @Test
   void fifteenFifteen() { // newly written test already green!
      // two options:
      // 1 you din't understand WHY it passes:
         // (a) your assertions are bad - bug in a test
         // (b) you already implemented this feature (you didnt follow pure TDD)  you were stupid
      // 2 you are smart > you took this "example" straight from the spec. (yes, I know they will be overlapping with other tests, but
      // i will leave it here for documentation purposes).
      String actual = translateScore(1, 1);

      assertThat(actual).isEqualTo("Fifteen:Fifteen");
   }

   @Test
   void deuce() {
      String actual = translateScore(3, 3);

      assertThat(actual).isEqualTo("Deuce");
   }

   @Test
   void deuce_whenBothPlayersScore4Points() {
      String actual = translateScore(4, 4);

      assertThat(actual).isEqualTo("Deuce");
   }

   @Test
   void advantagePlayer1_whenPlayer1Scored4_andPlayer2Scores3() {
      String actual = translateScore(4, 3);

      assertThat(actual).isEqualTo("Advantage Player1");
   }

   @Test
   void advantagePlayer1_whenPlayer1Scored7_andPlayer2Scores6() {
      String actual = translateScore(7, 6);

      assertThat(actual).isEqualTo("Advantage Player1");
   }




   // scores are *never* NEGATIVE. other examples: test with null arg, see what happens
}
