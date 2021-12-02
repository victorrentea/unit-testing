package victor.testing.tdd.outsidein;

import org.junit.jupiter.api.Test;
import victor.testing.tdd.outsidein.TennisGame.Player;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {
       // The running score of each game is described in a manner peculiar to tennis:
      // scores from zero to three points are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
   @Test
   void loveLove_givenANewGame() {

      // when
      String actual = TennisGame.getScore();

      // then
      assertThat(actual).isEqualTo("Love:Love"); // 💗 AssertJ library
   }

   @Test
   void fifteenLove_givenPlayer1Scores() {
      // given
      TennisGame.playerWonPoint(Player.ONE);

      // when
      String actual = TennisGame.getScore();

      // then
      assertThat(actual).isEqualTo("Fifteen:Love");
   }

   @Test
   void loveFiftee_givenPlayer2Scores() {
      // given
      TennisGame.playerWonPoint(Player.TWO);

      // when
      String actual = TennisGame.getScore();

      // then
      assertThat(actual).isEqualTo("Love:Fifteen");
   }

   // scores are *never* NEGATIVE. other examples: test with null arg, see what happens
}
