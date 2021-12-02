package victor.testing.tdd.outsidein;

public class TennisGame {

   private static String score = "Love:Love";

   enum Player {
      ONE,
      TWO,
   }


   public static String getScore() {
      return score;
   }
   public static void playerWonPoint(Player player) {
      if (player == Player.ONE) {
         score = "Fifteen:Love" ;
      }else {
         score = "Love:Fifteen" ;
      }
   }
}

