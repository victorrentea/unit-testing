package victor.testing.tdd.outsidein;

import static java.util.Objects.requireNonNull;

public class TennisGame {

   private int player1Points;
   private int player2Points;

   enum Player {
      ONE,
      TWO,
   }

   public String getScore() {
      if (player1Points >= 3 && player1Points == player2Points) {
         return "Deuce";
      }
      if (player1Points >= 3 && player2Points >= 3 && player1Points - player2Points == 1) {
         return "Advantage Player1";
      }
      return translate(player1Points) + ":" + translate(player2Points);
   }

   private String translate(int points) {
      switch (points) {
         case 0: return "Love";
         case 1: return "Fifteen";
         case 2: return "Thirty";
         case 3: return "Forty";
         default:
            throw new IllegalStateException("Unexpected value: " + points);
      }
   }


   public void playerWonPoint(/*@NonNull*/ Player player) {
      if (requireNonNull(player, "Player must not be null") == Player.ONE) {
         player1Points ++;
      } else {
         player2Points ++;
      }
   }
}

