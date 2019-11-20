package ro.victor.unittest.tdd;

public class TennisGame {
    private String score = "Love Love";
    private int player1Points;
    private int player2Points;

    public String score() {
        if (player1Points == player2Points &&
                player1Points >= 3) {
            return "Deuce";
        }
        return format(player1Points) + " " + format(player2Points);
    }

    private String format(int points) {
        switch (points) {
            case 0: return "Love";
            case 1: return "Fifteen";
            case 2: return "Thirty";
            default: return "Forty";
        }
    }

    public void point(int playerNo) {
        if (playerNo == 1) {
            player1Points ++;
//            score = "Fifteen Love";
        } else {
            player2Points ++;
//            score = "Love Fifteen";
        }
    }
}
