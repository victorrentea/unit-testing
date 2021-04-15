package victor.testing.tdd;

import java.util.HashMap;
import java.util.Map;

public class TennisScore {

   private int homePlayerScore;
   private int awayPlayerScore;

   public String getScore() {
      return translate(homePlayerScore) + " - " + translate(awayPlayerScore);
   }
   public String translate(int score) {
      switch (score) {
         case 0:
            return "Love";
         case 1:
            return "Fifteen";
         case 2:
            return "Thirty";
         default:
            throw new IllegalStateException("Unexpected value: " + score);
      }
   }

   public void addPoint(Player player) {
      if (player == Player.AWAY) {
         awayPlayerScore ++;
      } else {
         homePlayerScore++;
      }
   }
}
