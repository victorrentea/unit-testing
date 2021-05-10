package victor.testing.tdd;

public class TennisGame {

   private int player1Points;
   private int player2Points;

   public String getScore() {
      if (player1Points >= 3 && player2Points >= 3 && player1Points == player2Points) {
         return "Deuce";
      }
      if (player1Points >= 4 && player1Points - player2Points >= 2) {
         return "Game Won by Player 1";
      }
      return translate(player1Points) + " - " + translate(player2Points);
   }
   private String translate(int points) {
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
            throw new IllegalStateException("Unexpected value: " + points);
      }
   }

   public void addPoint(TennisParty party) {
      if (party == TennisParty.TWO) {
         player2Points++;
      } else {
         player1Points++;
      }
   }
}
