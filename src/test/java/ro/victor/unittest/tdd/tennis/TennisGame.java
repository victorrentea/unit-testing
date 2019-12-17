package ro.victor.unittest.tdd.tennis;

public class TennisGame {

    public static final String DELIMITER = " - ";
    private int scorePlayer1;
    private int scorePlayer2;

    public String score() {
        if (scorePlayer1 >= 3 && scorePlayer2 >= 3) {
            if (scorePlayer1 == scorePlayer2) {
                return "Deuce";
            }
            if (scorePlayer1 - scorePlayer2 == 1) {
                return "Advantage Player1";
            }
            if (scorePlayer2 - scorePlayer1 == 1) {
                return "Advantage Player2";
            }
        }
        return convert(scorePlayer1) + DELIMITER + convert(scorePlayer2);
    }

    private String convert(int points) {
        switch (points) {
            case 0:
                return "Love";
            case 1:
                return "Fifteen";
            case 2:
                return "Thirty";
            case 3:
                return "Forty";
            default:
                throw new IllegalStateException("Unexpected value: " + points);
        }
    }

    public void scorePoint(int player) {
        if (2 == player) {
            scorePlayer2++;
        } else {
            scorePlayer1++;
        }
    }
}
