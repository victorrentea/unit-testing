package victor.testing.tdd.classic;

public class TennisScore {

    private int player1Score;
    private int player2Score;

    public String getScore() {
        if (player1Score >=3 && player2Score >= 3
            && player1Score - player2Score == 1) {
            return "Advantage Player1";
        }
        if (player1Score == player2Score && player2Score >= 3) {
            return "Deuce";
        }
        return scoreAsText(player1Score) + " - " + scoreAsText(player2Score);
    }

    private String scoreAsText(int score) {
        switch (score) {
            case 0:
                return "Love";
            case 1:
                return "Fifteen";
            case 2:
                return "Thirty";
            default:
                return "Forty";
        }
        // uite ce vine in java 17 frate
//        return switch (score) {
//            case 0 -> "Love";
//            case 1 -> "Fifteen";
//            case 2 -> "Thirty";
//            default -> "Forty";
//        };
     }

    public void winPoint(int playerIndex) {
        if (playerIndex == 0) {
            player1Score ++;
        } else {
            player2Score ++;
        }
    }
}
