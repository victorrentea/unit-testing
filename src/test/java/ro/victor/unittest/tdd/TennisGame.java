package ro.victor.unittest.tdd;

public class TennisGame {

   private int player1Score;
   private int player2Score;

   public String score() {
      if (player1Score == player2Score && player1Score >= 3) {
         return "Deuce";
      }
      return translateScore(player1Score) + " - " + translateScore(player2Score);
   }
   private String translateScore(int points) {
      switch (points) {
         case 0:
            return "Love";
         case 1:
            return "Fifteen";
         case 2:
            return "Thirty";
         case 3:
            return "Forty";
         default:
            throw new IllegalArgumentException("Impossible to translate points " + points);
      }
   }

   public void markPointFor(Player player) {
      if (player == Player.ONE) {
         player1Score++;
      } else {
         player2Score++;
      }
   }

   public enum Player {
      TWO, ONE
   }
}
