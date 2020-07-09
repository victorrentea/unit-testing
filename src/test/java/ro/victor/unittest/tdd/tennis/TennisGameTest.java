package ro.victor.unittest.tdd.tennis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {


   private String resolveScore(int pointsPlayer1, int pointsPlayer2) {
      TennisGame tennisGame = new TennisGame();
      addPointsToPlayer(tennisGame, pointsPlayer1, 1);
      addPointsToPlayer(tennisGame, pointsPlayer2, 2);
      return tennisGame.score();
   }

   private void addPointsToPlayer(TennisGame tennisGame, int points, int playerNumber) {
      for (int i = 0; i < points; i++) {
         tennisGame.addPoint(playerNumber);
      }
   }

   @Test
   public void fifteenLove() {
      assertEquals("Fifteen - Love", resolveScore(1, 0));
   }

   @Test
   public void test() {
      assertEquals("Love - Love", resolveScore(0, 0));
   }
   @Test
   public void loveFifteen() {
      assertEquals("Love - Fifteen", resolveScore(0, 1));
   }
   @Test
   // il las desi de duplicat (crapa mereu cu cate un frate), niciodata singur,
   // dar il las pentru ca aduce valoare de documentare
   public void fifteenFifteen() {
      assertEquals("Fifteen - Fifteen", resolveScore(1, 1));
   }
   @Test
   public void thirtyLove() {
      assertEquals("Thirty - Love", resolveScore(2, 0));
   }

   @Test
   public void fortyLove() {
      assertEquals("Forty - Love", resolveScore(3, 0));
   }


//   @Test
//   public void gameWonByPlayer1() {
//      assertEquals("Game Won Player1", resolveScore(4, 0));
//      assertEquals("Game Won Player1", resolveScore(5, 3));
//   }

}
