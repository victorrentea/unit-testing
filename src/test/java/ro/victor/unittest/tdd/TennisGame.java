package ro.victor.unittest.tdd;

public class TennisGame {

    private int player1Score;
    private int player2Score;

    public String score() {

        if (player1Score >= 3 && player2Score >= 3 && player1Score == player2Score) {
            return "Deuce";
        }
        if (player1Score >= 3 && player2Score >= 3 &&
                player1Score == player2Score + 1) {
            return "Advantage Player1";
        }
        if (player1Score >= 3 && player2Score >= 3 &&
                player2Score == player1Score + 1) {
            return "Advantage Player2";
        }
        if (player1Score >= 4 && player1Score >= player2Score + 2) {
            return "Game won Player1";
        }
        if (player2Score >= 4 && player2Score >= player1Score + 2) {
            return "Game won Player2";
        }
        return getString(player1Score) + "-" + getString(player2Score);
    }

    private String getString(int score) {
        switch (score) {
            case 0:
                return "Love";
            case 1:
                return "Fifteen";
            case 2:
                return "Thirty";
            case 3:
                return "Forty";
            default:
                throw new IllegalStateException("Unexpected value: " + score);
        }
    }

    public void addPoint(int playerNumber) {
        if (playerNumber == 2) {
            player2Score ++;
        } else {
            player1Score ++;
        }
    }
}
