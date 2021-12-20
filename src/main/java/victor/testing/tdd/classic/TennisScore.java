package victor.testing.tdd.classic;

public class TennisScore {

   private int player1Score;
   private int player2Score;

   public String currentScore() {
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

   public void winsPointByPlayer1() {
      player1Score++;
   }

   public void winsPointByPlayer2() {
      player2Score++;
   }
}
