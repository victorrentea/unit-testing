package ro.victor.unittest.tdd.tennis;

public class TennisGame {

   private int score1;
   private int score2;
   private String scoreString = "Love - Love";

   public String score() {
      return scoreString;
   }

   public void addPoint(int playerNumber) {
      if (playerNumber == 1) {
         scoreString = "Fifteen - Love";
      } else {
         if (score().equals("Fifteen - Love")) {
            scoreString = "Fifteen - Fifteen";
         } else {
            scoreString = "Love - Fifteen";
         }
      }
   }
}


//   A game is won by the first player to have won
//   at least four points in total and at least two points more
//   than the opponent.

//    The running score of each game is described
//    in a manner peculiar to tennis: scores from zero
//    to three points are described as "Love", "Fifteen", "
//    Thirty", and "Forty" respectively.

//    If at least three points have been scored by each player,
//    and the scores are equal, the score is "Deuce".

//    If at least three points have been scored by each side
//    and a player has one more point than his opponent, the score
//    of the game is "Advantage" for the player in the lead.