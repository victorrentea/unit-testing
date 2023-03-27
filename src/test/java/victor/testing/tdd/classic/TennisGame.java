package victor.testing.tdd.classic;

public class TennisGame {

  // static ?!?
  private String score = "Love - Love";
  int player1Points = 0;
  int player2Points = 0;

  public String score() {
    String s = translatePoints(player1Points) + " - " + translatePoints(player2Points);

    return score;
  }

  private String translatePoints(int points) {
    if (points == 0) {
      return "Love";
    } else {
      return "Fifteen";
    }
  }

  public void earnsPoint(TennisSide side) {
    if (side == TennisSide.ONE) {
      player1Points ++ ;
      score = "Fifteen - Love";
    } else {
      player2Points ++;
//      if (score.equals("Love - Fifteen")) {
//        score = "Love - Thirty";
//      } else
//        score = "Love - Fifteen";
    }
  }
}
