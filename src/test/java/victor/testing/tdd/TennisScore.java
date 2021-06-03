package victor.testing.tdd;

public class TennisScore {

   private int player1Points;
   private int player2Points;

   public String getDisplayScore() {
      return translate(player1Points) + " - " + translate(player2Points);
   }

   private String translate(int points) {
      switch (points) {
         case 0: return "Love";
         case 1: return "Fifteen";
         case 2: return "Thirty";
         case 3: return "Forty";
         default: throw new IllegalArgumentException();
      }
   }

   public void scorePoint(Players player) {
      if (player == Players.ONE) {
         player1Points ++;
      } else {
         player2Points++;
      }
   }
}
