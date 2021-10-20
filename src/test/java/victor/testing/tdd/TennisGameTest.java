package victor.testing.tdd;

import org.junit.jupiter.api.Test;
import victor.testing.tdd.TennisScore.Player;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {

   // The running score of each game is described in a manner
   // peculiar to tennis: scores from zero to three points are
   // described as “Love”, “Fifteen”, “Thirty”, and “Forty”
   // respectively.
   @Test
   void loveLove_whenNewGame() {
      String actual = TennisScore.getScore();

      assertThat(actual).isEqualTo("Love-Love");
   }
   @Test
   void loveFifteen_whenFirstPlayerWinsAPoint() {
      TennisScore.winsPoint(Player.ONE);// design de API rafinam semnaturile

      String actual = TennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen-Love");
   }
}
