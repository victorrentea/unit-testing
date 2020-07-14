package ro.victor.unittest.tdd;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {

   private final TennisGame tennisGame = new TennisGame();

   @Test
   public void newGame() {
      assertThat(tennisGame.score()).isEqualTo("Love - Love");
   }

   @Test
   public void loveFifteen() {
       tennisGame.scoresPoint(Players.PLAYER2);
      assertThat(tennisGame.score()).isEqualTo("Love - Fifteen");
   }
   @Test
   public void fifteenLove() {
      tennisGame.scoresPoint(Players.PLAYER1);// b i e s
      assertThat(tennisGame.score()).isEqualTo("Fifteen - Love");
   }
   @Test
   public void thirtyLove() {
      // arrange
      tennisGame.scoresPoint(Players.PLAYER1);// b i e s
      tennisGame.scoresPoint(Players.PLAYER1);// b i e s

      // act
      String actualScore = tennisGame.score();

      // assert
      assertThat(actualScore).isEqualTo("Thirty - Love");
   }

}
