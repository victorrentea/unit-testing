package victor.testing.tdd;

public class TennisGame {

//   String[] arr = {"Love", "Fifteen", };
//   Map<Integer, ClasaScoreSiLabel> labels; ??
   public enum Player {
      ONE, TWO
   }
   private int player1Score;
   private int player2Score;


   public String score() {
      if (player1Score >= 3 && player2Score == player1Score) {
         return "Deuce";
      }
      return translateScore(player1Score) + " - " +
             translateScore(player2Score);
   }

   private String translateScore(int score) {
      switch (score) {
         case 0:
            return "Love";
         case 1:
            return "Fifteen";
         case 2:
            return "Thirty";
         default:
            return "Forty";
      }
   }

   public void playerScores(Player playerNumber) {
      if (playerNumber == Player.TWO) {
         player2Score ++;
      } else if(playerNumber == Player.ONE) {
         player1Score ++;
      }
   }
}
