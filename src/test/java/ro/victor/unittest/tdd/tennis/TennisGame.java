package ro.victor.unittest.tdd.tennis;

public class TennisGame {
   private final static String[] LABELS = {"Love", "Fifteen", "Thirty", "Forty"};

   private int score1;
   private int score2;

   public String score() {
      if (score1 >= 3 && score2 >= 3) {
         return "Deuce";
      }
//      if (score1 >= 4/* && score1 - score2 >= 2*/) {
//         return "Game Won Player1";
//      }
      return LABELS[score1] + " - " + LABELS[score2];
   }

   public void addPoint(int playerNumber) {
      if (playerNumber == 1) {
         score1 ++;
      } else {
         score2 ++;
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