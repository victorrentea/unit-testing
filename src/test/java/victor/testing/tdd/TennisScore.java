package victor.testing.tdd;

public class TennisScore {
  public static final String[] SCORES = {"Love", "Fifteen", "Thirty", "Forty"};
  private int player1Score;
  private int player2Score;

  public String score() {
    if (player1Score >= 4 && player1Score - player2Score >= 2) {
      return "Game won Player1";
    } else if (player2Score >= 4 && player2Score - player1Score >= 2) {
      return "Game won Player2";
    }
    if (player1Score >= 3 && player2Score >= 3) {
      if (player1Score == player2Score) {
        return "Deuce";
      } else if (player1Score == player2Score + 1) {
        return "Advantage Player1";
      } else if (player2Score == player1Score + 1) {
        return "Advantage Player2";
      } else {
        throw new RuntimeException("Impossible!");
      }
    } else {
      return SCORES[player1Score] + "-" + SCORES[player2Score];
    }
  }

  public void addPoint(int playerNumber) {
    if (playerNumber == 1) {
      player1Score++;
    } else {
      player2Score++;
    }
  }
}
