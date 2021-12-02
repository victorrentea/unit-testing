package victor.testing.tdd.outsidein;

public class TennisGame {

   private String score = "Love:Love";

   enum Player {
      ONE,
      TWO,
   }


   public String getScore() {
      return score;
   }
   public void playerWonPoint(Player player) {
      if (player == Player.ONE) {
         score = "Fifteen:Love" ;
      }else {
         score = "Love:Fifteen" ;
      }
   }
}

