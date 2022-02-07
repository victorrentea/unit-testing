package victor.testing.tdd.classic;

import java.util.Map;

public class TennisGame {

   private int player2Points;
   private int player1Points;

   private static final Map<Integer, String> POINTS_TO_SCORE = Map.of(
       0, "Love",
       1, "Fifteen",
       2, "Thirty",
       3, "Forty"
   );

   private String mapper(int points) {
      return POINTS_TO_SCORE.get(points);
   }

   public String getScore() {
      return mapper(player1Points) + " - " + mapper(player2Points);
   }

   public void addPoint(Player player) {
      if (player == Player.ONE) {
         player1Points ++ ;
      } else {
         player2Points ++;
      }
   }
}
