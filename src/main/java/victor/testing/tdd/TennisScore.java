package victor.testing.tdd;

import java.util.HashMap;
import java.util.Map;

public class TennisScore {

   private int homePlayerScore;
   private int awayPlayerScore;
   private static final Map<Integer, String > scoreMap = new HashMap<>();
   static {
      scoreMap.put(1, "Fifteen");
   }

   public String getScore() {
      return translate(homePlayerScore) + " - " + translate(awayPlayerScore);
   }
   public String translate(int score) {
//      return scoreMap.getOrget(score);
      return switch (score) {
         case 0 -> "Love";
         case 1 -> "Fifteen";
         case 2 -> "Thirty";
         default -> throw new IllegalStateException("Unexpected value: " + score);
      };
   }

   public void addPoint(Player player) {
      if (player == Player.AWAY) {
         awayPlayerScore ++;
      } else {
         homePlayerScore++;
      }
   }
}
