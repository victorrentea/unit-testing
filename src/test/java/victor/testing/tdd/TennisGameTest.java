package victor.testing.tdd;

import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import victor.testing.tdd.TennisGame.Player;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {


   @Test
   public void loveLove() {
      String score = new TennisGame().score();
      assertEquals("Love - Love",score);
   }
   @Test
   public void loveFifteen() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.playerScores(Player.TWO);
      String score = tennisGame.score();
      assertEquals("Love - Fifteen",score);
   }
   @Test
   public void loveThirty() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      String score = tennisGame.score();
      assertEquals("Love - Thirty",score);
   }
   @Test
   public void loveForty() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      String score = tennisGame.score();
      assertEquals("Love - Forty",score);
   }
   @Test
   public void fifteenLove() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.playerScores(Player.ONE);
      String score = tennisGame.score();
      assertEquals("Fifteen - Love",score);
   }

   // TEST OVERLAP: In general de evitat.
   // Definitie: cade mereu alte teste impreuna. Niciodata singur.

   // TOTUSI, il poti lasa daca este uncaz de business fenomenal de interesant
   @Test
   public void thirtyThirty() {
      TennisGame tennisGame = new TennisGame();
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.TWO);
      tennisGame.playerScores(Player.ONE);
      tennisGame.playerScores(Player.ONE);
      String score = tennisGame.score();
      assertEquals("Thirty - Thirty",score);
   }





   // Mai tarziu: test de inputuri invalide: sa arunce exceptie daca strange mai mult de 3 puncte
//   public void boundaryTest() { }
}
