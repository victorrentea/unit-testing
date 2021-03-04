package victor.testing.tdd;

import org.junit.Before;
import org.junit.Test;
import victor.testing.tdd.TennisGame.Player;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {


   private TennisGame tennisGame = new TennisGame();

   public TennisGameTest() {
      System.out.println("Cate instante de clasa de test se creeaza");
   }

   @Test
   public void loveLove() {
      String score = tennisGame.score();
      assertEquals("Love - Love",score);
   }
   @Test
   public void loveFifteen() {
      tennisGame.playerScores(Player.TWO);
      String score = tennisGame.score();
      assertEquals("Love - Fifteen",score);
   }
   @Test
   public void loveThirty() {
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      String score = tennisGame.score();
      assertEquals("Love - Thirty",score);
   }
   @Test
   public void loveForty() {
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      String score = tennisGame.score();
      assertEquals("Love - Forty",score);
   }
   @Test
   public void fifteenLove() {
      tennisGame.playerScores(Player.ONE);
      String score = tennisGame.score();
      assertEquals("Fifteen - Love",score);
   }

   // TEST OVERLAP: In general de evitat.
   // Definitie: cade mereu alte teste impreuna. Niciodata singur.
   // TOTUSI, il poti lasa daca este uncaz de business fenomenal de interesant

   @Test
   public void thirtyThirty() {
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.ONE);
      tennisGame.playerScores(Player.ONE);
      String score = tennisGame.score();
      assertEquals("Thirty - Thirty",score);
   }

   @Test
   public void deuce() {
      tennisGame.playerScores(Player.ONE);
      tennisGame.playerScores(Player.ONE);
      tennisGame.playerScores(Player.ONE);
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      String score = tennisGame.score();
      assertEquals("Deuce",score);
   }

   @Test
   public void deuce4() {
      playerOneScores4Points(tennisGame);
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      String score = tennisGame.score();
      assertEquals("Deuce",score);
   }

   private void playerOneScores4Points(TennisGame tennisGame) {
      for (int i = 0; i < 4; i++) {
         tennisGame.playerScores(Player.ONE);
      }
   }


   // Mai tarziu: test de inputuri invalide: sa arunce exceptie daca strange mai mult de 3 puncte
//   public void boundaryTest() { }
}
