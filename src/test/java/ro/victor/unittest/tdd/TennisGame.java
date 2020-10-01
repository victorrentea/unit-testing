package ro.victor.unittest.tdd;

public class TennisGame {

   private int player1Score;
   private int player2Score;

   public String score() {
      return translateScore(player1Score) + " - Love";
   }
   public String translateScore(int points) {
      if (points == 0) {
         return "Love";
      }
      if (points == 1) {
         return "Fifteen";
      }
      if (points == 2) {
         return "Thirty";
      }
      throw new IllegalArgumentException("Impossible to translate points " + points);
   }

   public void markPointFor(Players player) {
      if (player == Players.ONE) {
         player1Score++;
      } else {
         player2Score++;
      }
   }

   public enum Players {
      TWO, ONE
   }
}
