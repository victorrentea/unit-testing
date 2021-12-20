package victor.testing.tdd.classic;

public class TennisScore {

   private String state = "Love-Love";

   public String currentScore() {
      return state;
   }

   public void winsPointByPlayer1() {
      if (state == "Fifteen-Love") {
         state = "Thirty-Love";
      } else {
         state = "Fifteen-Love";
      }
   }

   public void winsPointByPlayer2() {
      state = "Love-Fifteen";
   }
}
