package victor.testing.tennis;

public class TennisScore {
  private Integer player1Score = 0;
  private Integer player2Score = 0;

  public enum Player {
    ONE, TWO
  }

  public String getScoreDescription() {
    if (player1Score>=4) {
      if (player1Score - player2Score == 2) {
        return "Game Won by Player1";
      }
      if (player1Score-player2Score==1) return "Advantage Player1";
    }
    String player1ScoreName = getScoreDescription(player1Score);
    String player2ScoreName = getScoreDescription(player2Score);
    return player1ScoreName + "-" + player2ScoreName;
  }

  public void addAPointToPlayersId(Player playerId) {
    switch(playerId){
      case ONE:
        player1Score++;
        break;
      case TWO:
        player2Score++;
        break;
      // NO DEFAULT!
    }
  }
  private String getScoreDescription(int points) {
    switch (points) {
      case 0:
        return "Love";
      case 1:
        return "Fifteen";
      case 2:
        return "Thirty";
      case 3:
        return "Forty";
      default:// UNTESTED! are we sure?
        throw new IllegalArgumentException("Impossible!! Not playing tennis over there!");
    }
  }

}
