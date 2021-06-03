package victor.testing.tdd;

public class TennisScore {

   private String score = "Love - Love";
   private String player1Score = "Love";
   private String player2Score = "Love";

   public String getScore() {
      return score;
   }

   public void scorePoint(Players player) {
      if (player == Players.ONE) {
         score = "Fifteen - Love";
      } else {
         score = "Love - Fifteen";
      }
   }
}
