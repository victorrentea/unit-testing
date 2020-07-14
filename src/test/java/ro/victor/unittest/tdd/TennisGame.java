package ro.victor.unittest.tdd;

public class TennisGame {

   private static String scoreString = "Love - Love";

   public static String score() {
      return scoreString;
   }

   public static void scoresPoint(Players player) {
      scoreString = "Fifteen - Love";
   }
}
// The running score of each game is
// described in a manner peculiar to tennis:
// scores from zero to three points are described
// as "Love", "Fifteen", "Thirty", and "Forty"
// respectively.