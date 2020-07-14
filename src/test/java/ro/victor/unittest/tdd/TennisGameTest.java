package ro.victor.unittest.tdd;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {

   @Test
   public void newGame() {
      String s = new TennisGame().score();
      assertThat(s).isEqualTo("Love - Love");
   }

   @Test
   public void fifteenLove() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.scoresPoint(Players.PLAYER1);// b i e s
      String s = tennisGame.score();
      assertThat(s).isEqualTo("Fifteen - Love");
   }
   @Test
   public void thirtyLove() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.scoresPoint(Players.PLAYER1);// b i e s
      tennisGame.scoresPoint(Players.PLAYER1);// b i e s
      String s = tennisGame.score();
      assertThat(s).isEqualTo("Thirty - Love");
   }
}
