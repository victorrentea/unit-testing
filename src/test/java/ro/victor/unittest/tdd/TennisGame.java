package ro.victor.unittest.tdd;

public class TennisGame {

   private int player1Score;
   private int player2Score;

   public String score() {
      String s;
      if (player1Score == 0) {
         s = "Love - Love";
      } else if (player1Score == 1) {
         s = "Fifteen - Love";
      } else if (player1Score == 2) {
         s = "Thirty - Love";
      } else {
         throw new IllegalArgumentException();
      }
      return s;
   }

   public void scoresPoint(Players player) {
      player1Score ++;
   }
}
// The running score of each game is
// described in a manner peculiar to tennis:
// scores from zero to three points are described
// as "Love", "Fifteen", "Thirty", and "Forty"
// respectively.