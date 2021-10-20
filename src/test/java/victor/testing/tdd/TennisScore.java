package victor.testing.tdd;

public class TennisScore {

   private int player1Points = 0;
   private int player2Points = 0;

   enum Player {
      ONE, TWO;
   }


   public String getScore() {
      if (player1Points >= 4 && player1Points - player2Points >= 2) {
         return "Game won Player 1";
      }
      if (player1Points >= 3 && player2Points >= 3) {
         if (player1Points == player2Points) {
            return "Deuce";
         }
         if (player2Points - player1Points == 1) {
            return "Advantage Player 2";
         }
         if (player1Points - player2Points == 1) {
            return "Advantage Player 1";
         }
      }
      return getScore(player1Points) + "-" + getScore(player2Points);
   }

//   private static final Map<Integer, String> SCORE_STRINGS = Map.of(
//       0, "Love",
//       1, "Fifteen",
//       2, "Thirty"
//   );
   private static final String[] SCORE_STRINGS = {"Love", "Fifteen", "Thirty"};

   private String getScore(int points) {
//      return SCORE_STRINGS[points];
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

   public void winsPoint(Player player) {
      if (player == Player.ONE) {
         player1Points++;
      } else {
         player2Points++;
      }
   }
}
