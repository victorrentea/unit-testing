package ro.victor.unittest.tdd;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {

   @Test
   public void newGame() {
       String s = TennisGame.score();
       assertThat(s).isEqualTo("Love - Love");
   }
   @Test
   public void fifteenLove() {
      TennisGame.scoresPoint(Players.PLAYER1);// b i e s
       String s = TennisGame.score();
       assertThat(s).isEqualTo("Fifteen - Love");
   }
}
