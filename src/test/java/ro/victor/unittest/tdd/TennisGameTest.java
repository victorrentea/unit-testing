package ro.victor.unittest.tdd;

import org.junit.Test;
import ro.victor.unittest.tdd.TennisGame.Player;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {


   public TennisGameTest() {
      System.out.println("Oare de cate ori se instantiaza clasa asta de test");
   }

   private String getScoreString(int player1Points, int player2Points) {
      TennisGame tennisGame = new TennisGame();
      markPointsForPlayer(Player.ONE, player1Points, tennisGame);
      markPointsForPlayer(Player.TWO, player2Points, tennisGame);
      return tennisGame.score();
   }

   private void markPointsForPlayer(Player player, int points, TennisGame tennisGame) {
      for (int i = 0; i < points; i++) {
         tennisGame.markPointFor(player);
      }
   }

   @Test
   public void initialGame() {
      assertEquals("Love - Love", getScoreString(0,0));
      assertEquals("Fifteen - Love", getScoreString(1,0));
      assertEquals("Thirty - Love", getScoreString(2,0));
   }

   @Test
   public void player2Wins1Point() {
      assertEquals("Love - Fifteen", getScoreString(0,1));
   }

   @Test
   public void player2Wins2Point_overlapping_todelete() { // to delete
      assertEquals("Love - Thirty", getScoreString(0,2));
   }

   @Test // remarkable business case that deserves a dedicated test to PROVE that THIS CAN BE DONE
   public void bothPlayersWin1Point_overlapping_toKEEP() { // to delete
      assertEquals("Fifteen - Fifteen", getScoreString(1,1));
   }

   @Test
   public void player1Wins3Points() {
      assertEquals("Forty - Love", getScoreString(3,0));
   }

   @Test
   public void deuce() {
      assertEquals("Deuce", getScoreString(3, 3));
   }

   @Test
   public void deuce5() {
      assertEquals("Deuce", getScoreString(5, 5));
   }

}
