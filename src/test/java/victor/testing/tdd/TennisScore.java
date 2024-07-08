package victor.testing.tdd;

import java.util.Map;

public class TennisScore {

  private String string = "Love - Love";
  private int player1Points = 0;
//  private static final Map<Integer, String> scores = Map.of(
//      0, "Love",
//      1, "Fifteen"
//  );
  private static final String[] LABELS = {"Love", "Fifteen", "Thirty"};
//  enum Label{Love, Fifteen, Thirty}
  public String getScore() {
//    Label.values()[0].name()
    String decoded = LABELS[player1Points];
    return string;
  }

  public void addPointToPlayer1() {
    player1Points++;
    if (string.equals("Love - Love")) {
      string = "Fifteen - Love";
    } else if (string.equals("Fifteen - Love")) {
      string = "Thirty - Love";
    }
  }
}
