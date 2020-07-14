package ro.victor.unittest.tdd;

public class TennisGame {

   private int player1Score;
   private int player2Score;

   public String score() {
      return translate(player1Score) + " - " + translate(player2Score);
   }

   private String translate(int score) {
      switch (score) {
         case 0:
            return "Love";
         case 1:
            return "Fifteen";
         case 2:
            return "Thirty";
         default:
            throw new IllegalArgumentException();
      }
   }

   public void scoresPoint(Players player) {
      if (player == Players.PLAYER1) {
         player1Score++;
      } else {
         player2Score++;
      }
   }
}
// The running score of each game is
// described in a manner peculiar to tennis:
// scores from zero to three points are described
// as "Love", "Fifteen", "Thirty", and "Forty"
// respectively.