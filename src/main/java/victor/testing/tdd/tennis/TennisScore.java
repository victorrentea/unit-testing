package victor.testing.tdd.tennis;

import org.jetbrains.annotations.NotNull;

public class TennisScore {

  private int player1Points = 0;
  private int player2Points = 0;
//  private static final Map<Integer, String> pointsToScore = Map.of(
//      0, "Love",
//      1, "Fifteen",
//      2, "Thirty",
//      3, "Forty"
//      );
  private static final String[] LABELS = {"Love", "Fifteen","Thirty","Forty"};

  public String getScore() {
    if (player1Points == player2Points && player1Points >= 3) {
      return "Deuce";
    }
    return translate(player1Points) + "-" + translate(player2Points);
  }

  @NotNull
  private String translate(int points) {
//    switch (points) {
//      case 0 :
//        return "Love";
//
//    }
    return LABELS[points];
  }

  public void player1Scored() {
    player1Points++;
  }

  public void player2Scored() {
    player2Points++;
  }
}
