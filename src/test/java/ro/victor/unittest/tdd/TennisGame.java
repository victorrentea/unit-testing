package ro.victor.unittest.tdd;

public class TennisGame {

   private String score = "Love - Love";

   public String score() {
      return score;
   }

   public void markPointFor(Players player) {
      if (score.equals("Fifteen - Love")) {
         score = "Thirty - Love";
      } else {
         score = "Fifteen - Love";
      }
   }

   public enum Players {
      ONE
   }
}
