package ro.victor.unittest.tdd.tennis;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {
   @Test
   public void test() {
      assertEquals("Love - Love", new TennisGame().score());
   }
   @Test
   public void fifteenLove() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.addPoint(1);
      assertEquals("Fifteen - Love", tennisGame.score());
   }
   @Test
   public void loveFifteen() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.addPoint(2);
      assertEquals("Love - Fifteen", tennisGame.score());
   }

   @Test
   // il las desi de duplicat (crapa mereu cu cate un frate), niciodata singur,
   // dar il las pentru ca aduce valoare de documentare
   public void fifteenFifteen() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.addPoint(1);
      tennisGame.addPoint(2);
      assertEquals("Fifteen - Fifteen", tennisGame.score());
   }
   @Test
   public void thirtyLove() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.addPoint(1);
      tennisGame.addPoint(1);
      assertEquals("Thirty - Love", tennisGame.score());
   }
   @Test
   public void fortyLove() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.addPoint(1);
      tennisGame.addPoint(1);
      tennisGame.addPoint(1);
      assertEquals("Forty - Love", tennisGame.score());
   }
   @Test
   public void gameWonByPlayer1() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.addPoint(1);
      tennisGame.addPoint(1);
      tennisGame.addPoint(1);
      tennisGame.addPoint(1);
      assertEquals("Game Won Player1", tennisGame.score());
   }
}
