package ro.victor.unittest.tdd.tennis;

public class TennisGame {

    public static final String DELIM = " - ";
    public static final String[] numberToString =
            {"Love", "Fifteen", "Thirty", "Forty"};
    private int player1Score;
    private int player2Score;

    public String score() {
        if (player1Score >= 4 || player2Score >= 4) {
            if (player1Score - player2Score >= 2) {
                return "Win Player 1";
            }
            if (player2Score - player1Score >= 2) {
                return "Win Player 2";
            }
        }
        if (player1Score >= 3 && player2Score >= 3) {
            if (player1Score == player2Score + 1) {
                return "Advantage Player 1";
            }
            if (player2Score == player1Score + 1) {
                return "Advantage Player 2";
            }
            if (player1Score == player2Score) {
                return "Deuce";
            }
        }
        return numberToString[player1Score]
                + DELIM + numberToString[player2Score];
    }

    public void playerScores(int playerNumber) {
        if (playerNumber == 2) {
            player2Score++;
        } else {
            player1Score++;
        }
    }
}
