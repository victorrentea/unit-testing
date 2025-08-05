package victor.testing.tennis;

public class TennisScore {
    private int player1Score = 0;
    private int player2Score = 0;

    public String getScore() {
        return translate(player1Score) + "-" +
               translate(player2Score);
    }

    private String translate(int player1Score) {
        return switch (player1Score) {
            case 1 -> "Fifteen";
            case 2 -> "Thirty";
            case 3 -> "Forty";
            default -> "Love";
        };
    }

    public void addPoint(int playerIndex) {
        if (playerIndex==0) {
          player1Score++;
        } else {
          player2Score++;
        }
    }
}