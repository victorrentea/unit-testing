package ro.victor.unittest.tdd;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {
   @Test
   public void initialGame() {
      String score = new TennisGame().score();
      assertEquals("Love - Love", score);
   }
   @Test
   public void player1Wins1Point() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.markPointFor(TennisGame.Players.ONE);
      String score = tennisGame.score();
      assertEquals("Fifteen - Love", score);
   }

   @Test
   public void player1Wins2Points() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.markPointFor(TennisGame.Players.ONE);
      tennisGame.markPointFor(TennisGame.Players.ONE);
      String score = tennisGame.score();
      assertEquals("Thirty - Love", score);
   }
   public void player2Wins1Point() {}
}
