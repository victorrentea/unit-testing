package ro.victor.unittest.tdd;

public class TennisGame {
    private static final String[] SCORE_NAMES = {"Love", "Fifteen", "Thirty", "Forty"};

    private int score1;
    private int score2;


    public String score() {
        if (score1 >= 3 && score2 >= 3) {
            if (score1 == score2) {
                return "Deuce";
            }
            if (score1 == score2 + 1) {
                return "Advantage Player 1";
            }
            if (score2 == score1 + 1) {
                return "Advantage Player 2";
            }
        }
        if (score1 - score2 >= 2 && score1 >= 4) {
            return "Game Won Player 1";
        }
        if (score2 - score1 >= 2 && score2 >= 4) {
            return "Game Won Player 2";
        }
        return SCORE_NAMES[score1]
                + "-" +
                SCORE_NAMES[score2];
    }

    public void point(Player player) {
        if (player == Player.ONE) {
            score1++;
        } else {
            score2++;
        }
    }
}
