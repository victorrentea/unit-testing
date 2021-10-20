package victor.testing.tdd;

public class TennisScore {

   private int player1Points = 0;
   private int player2Points = 0;

   enum Player {
      ONE, TWO;
   }


   public String getScore() {
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
         default:
            return "Thirty";
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
