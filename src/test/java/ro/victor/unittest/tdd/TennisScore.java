package ro.victor.unittest.tdd;

public class TennisScore {
   private int player1Score;
   private int player2Score;

   public String score() {
      String score = translate();
      score += "-Love";
      return score;
   }

   private String translate() {
      switch (player1Score) {
         case 2:
            return "Thirty";
         case 1:
            return "Fifteen";
         default:
            return "Love";
      }
   }

   public void pointWon(Player player) {
      player1Score++;
   }
}
