package victor.testing.tdd;

import org.junit.Before;
import org.junit.Test;
import victor.testing.tdd.TennisGame.Player;

import static org.junit.Assert.assertEquals;
import static victor.testing.tdd.TennisGame.Player.ONE;
import static victor.testing.tdd.TennisGame.Player.TWO;

public class TennisGameTest {

   private TennisGame tennisGame = new TennisGame();

   private void setScore(int player1Points, int player2Points) {
      scorePoints(ONE, player1Points);
      scorePoints(TWO, player2Points);
   }

   // mini-test framework
   private void scorePoints(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.playerScores(player);
      }
   }
   private String translateScore(int player1Score, int player2Score) {
      setScore(player1Score, player2Score);
      return tennisGame.score();
   }


   @Before
   public final void before() {
       // insert user in db
      // mockuieste pe alal
      // creaza datele alrea
      // pune fisierul acolo
//      mockuiest si pa-al
       // insert user in db
      // mockuieste pe alal
      // creaza datele alrea
      // pune fisierul acolo
//      mockuiest si pa-al

   }

   // 20-50 teste,



   @Test
   public void loveLove() {
      String score = translateScore(0, 0);
      assertEquals("Love - Love",score);
   }
   @Test
   public void loveFifteen() {
      String score = translateScore(0, 1);
      assertEquals("Love - Fifteen",score);
   }
   @Test
   public void loveThirty() {
      String score = translateScore(0, 2);
      assertEquals("Love - Thirty",score);
   }
   @Test
   public void loveForty() {
      String score = translateScore(0, 3);
      assertEquals("Love - Forty",score);
   }
   @Test
   public void fifteenLove() {
      String score = translateScore(1, 0);
      assertEquals("Fifteen - Love",score);
   }

   // TEST OVERLAP: In general de evitat.
   // Definitie: cade mereu alte teste impreuna. Niciodata singur.
   // TOTUSI, il poti lasa daca este uncaz de business fenomenal de interesant

   @Test
   public void thirtyThirty() {
      String score = translateScore(2, 2);
      assertEquals("Thirty - Thirty",score);
   }

   @Test
   public void deuce() {
      String score = translateScore(3, 3);
      assertEquals("Deuce",score);
   }

   @Test
   public void deuce4() {
      String score = translateScore(4, 4);
      assertEquals("Deuce",score);
   }



   // A game is won by the first player to have scored at
   // least 4 points in total and at least 2 points
   // more than the opponent.

   @Test
   public void gameWonPlayer1() {
      String score = translateScore(4, 2);
      assertEquals("Game Won Player 1", score);
   }
   @Test
   public void gameWonPlayer1_over4() {
      String score = translateScore(5, 3);
      assertEquals("Game Won Player 1", score);
   }
   @Test
   public void gameWonPlayer1_batut_mar() {
      String score = translateScore(4, 0);
      assertEquals("Game Won Player 1", score);
   }
   // TODO game won player2

   // If at least three points have been scored by each side
   // and a player has one more point than his opponent,
   // the score of the game is "Advantage" for the player in the lead.
   @Test
   public void advantagePlayer1() {
      String score = translateScore(4,3);
      assertEquals("Advantage Player 1", score);
   }
   @Test
   public void advantagePlayer1_later() {
      String score = translateScore(7,6);
      assertEquals("Advantage Player 1", score);
   }
   @Test
   public void advantagePlayer2() {
      String score = translateScore(3,4);
      assertEquals("Advantage Player 2", score);
   }
   @Test
   public void advantagePlayer2_later() {
      String score = translateScore(6,7);
      assertEquals("Advantage Player 2", score);
   }


   // Mai tarziu: test de inputuri invalide: sa arunce exceptie daca strange mai mult de 3 puncte
//   public void boundaryTest() { }
}
