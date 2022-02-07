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
      if (player1Points >= 3 && player1Points == player2Points) {
         return "Deuce";
      }
      if (advantageFor(player2Points, player1Points)) {
         return "Advantage Player2";
      }
      if (advantageFor(player1Points, player2Points)) {
         return "Advantage Player1";
      }
      return mapper(player1Points) + " - " + mapper(player2Points);
   }

   private boolean advantageFor(int leadingPlayer, int otherPlayer) {
      return otherPlayer >= 3 && leadingPlayer >= 3 && leadingPlayer - otherPlayer == 1;
   }

   public void addPoint(Player player) {
      if (player == Player.ONE) {
         player1Points ++ ;
      } else {
         player2Points ++;
      }
   }
}
