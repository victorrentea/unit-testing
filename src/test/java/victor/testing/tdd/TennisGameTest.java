package victor.testing.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TennisGameTest {


   private TennisGame tennisGame = new TennisGame();

   public TennisGameTest() {
      System.out.println("HOW MANY");
   }

   @Test
   public void newGame() {
      setScore(0, 0);
      String score = tennisGame.getScore();
      assertEquals("Love - Love", score);
   }

   @Test
   public void loveFifteen() {
      setScore(0,1);
      String score = tennisGame.getScore();
      assertEquals("Love - Fifteen", score);
   }

   @Test
   public void fifteenLove() {
      setScore(1,0);
      String score = tennisGame.getScore();
      assertEquals("Fifteen - Love", score);
   }
   @Test
   public void thirtyLove() {
     setScore(2,0);
      String score = tennisGame.getScore();
      assertEquals("Thirty - Love", score);
   }
   @Test
   public void fortyLove() {
      setScore(3,0);
      String score = tennisGame.getScore();
      assertEquals("Forty - Love", score);
   }
   @Test
   public void fifteenFifteen_FROM_BIZ$$$$$() {
      // KEPT, even though is redudant, as it is an example in the spec
     setScore(1,1);
      String score = tennisGame.getScore();
      assertEquals("Fifteen - Fifteen", score);
   }


   //If at least three points have been scored by each player,
   // and the scores are equal, the score is “Deuce”.
   @Test
   public void deuce() {
      setScore(3, 3);
      String score = tennisGame.getScore();
      assertEquals("Deuce", score);
   }
   //If at least three points have been scored by each player,
   // and the scores are equal, the score is “Deuce”.
   @Test
   public void deuce5() {
      setScore(5, 5);
      String score = tennisGame.getScore();
      assertEquals("Deuce", score);
   }

   @Test
   public void gameWonPlayerOne() {
       // A game is won by the first player to have won at
      // least four points in total and
      // at least two points more than the opponent.
      setScore(4, 2);
      String score = tennisGame.getScore();
      assertEquals("Game Won by Player 1", score);
   }
   @Test
   public void gameWonPlayerOneMorePoints() {
      setScore(14, 12);
      String score = tennisGame.getScore();
      assertEquals("Game Won by Player 1", score);
   }

   private void setScore(int player1Score, int player2Score) {
      addPointsToParty(player1Score, TennisParty.ONE);
      addPointsToParty(player2Score, TennisParty.TWO);
   }

   private void addPointsToParty(int points, TennisParty party) {
      for (int i = 0; i < points; i++) {
         tennisGame.addPoint(party);
      }
   }
}
