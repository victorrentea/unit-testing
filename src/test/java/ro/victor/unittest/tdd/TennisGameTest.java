package ro.victor.unittest.tdd;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {

   private TennisGame tennisGame = new TennisGame();

   public TennisGameTest() {
      System.out.println("Oare de cate ori se instantiaza clasa asta de test");
   }

   @Test
   public void initialGame() {
      String score = tennisGame.score();
      assertEquals("Love - Love", score);
   }
   @Test
   public void player1Wins1Point() {
      tennisGame.markPointFor(TennisGame.Players.ONE);
      String score = tennisGame.score();
      assertEquals("Fifteen - Love", score);
   }

   @Test
   public void player1Wins2Points() {
      tennisGame.markPointFor(TennisGame.Players.ONE);
      tennisGame.markPointFor(TennisGame.Players.ONE);
      String score = tennisGame.score();
      assertEquals("Thirty - Love", score);
   }
   @Test
   public void player2Wins1Point() {
      tennisGame.markPointFor(TennisGame.Players.TWO);
      String score = tennisGame.score();
      assertEquals("Love - Fifteen", score);
   }
}
