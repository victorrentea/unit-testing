package ro.victor.unittest.tdd;

public class TennisGame {

   private String scoreString = "Love - Love";

   public String score() {
      return scoreString;
   }

   public void scoresPoint(Players player) {

      if (scoreString.equals("Fifteen - Love")) {
         scoreString = "Thirty - Love";
      } else {
         scoreString = "Fifteen - Love";
      }
   }
}
// The running score of each game is
// described in a manner peculiar to tennis:
// scores from zero to three points are described
// as "Love", "Fifteen", "Thirty", and "Forty"
// respectively.