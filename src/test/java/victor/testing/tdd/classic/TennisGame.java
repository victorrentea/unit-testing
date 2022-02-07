package victor.testing.tdd.classic;

public class TennisGame {

   private String string = "Love - Love";

   public String getScore() {
      return string;
   }

   public void addPoint(Player player) {
      string = "Love - Fifteen";
   }
}
