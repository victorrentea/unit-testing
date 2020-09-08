package ro.victor.unittest.tdd;

public class TennisScore {
   private int player1Score;
   private int player2Score;

   public String score() {
      if (player1Score >= 3 && player1Score == player2Score) {
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

   public void pointWon(Player player) {
      if (player == Player.ONE) {
         player1Score++;
      } else {
         player2Score++;
      }
   }
}
