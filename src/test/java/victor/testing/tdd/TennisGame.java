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
      if (player1Score >= 3 && player2Score >= 3) {
         if (player1Score - player2Score == 1) {
            return "Advantage Player 1";
         }
         if (player2Score - player1Score == 1) {
            return "Advantage Player 2";
         }
         if (player2Score == player1Score) {
            return "Deuce";
         }
      }
      if (player1Score >= 4 && player1Score - player2Score >= 2) {
         return "Game Won Player 1";
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
         case 3:
            return "Forty";

         default:
            throw new IllegalStateException("Unexpected value: " + score);
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
