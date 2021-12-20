package victor.testing.tdd.classic;

public class TennisScore {

   private int player1Score;
   private int player2Score;
   public enum Player {
      ONE, TWO
   }

   public String currentScore() {
      if (player1Score == player2Score && player1Score>= 3) {
         return "Deuce";
      }
      return translate(player1Score) + "-" + translate(player2Score);
   }

   private String translate(int score) {
      switch (score) {
         case 0:
            return "Love";
         case 1:
            return "Fifteen";
         case 2:
            return "Thirty";
         case 3:
            return "Forty";
         default:
            throw new IllegalStateException("Unexpected value: " + score);
      }
   }

   public void winsPointByPlayer(Player player) {
      if (player == Player.ONE) {
         player1Score++;
      } else {
         player2Score++;
      }
   }

}
