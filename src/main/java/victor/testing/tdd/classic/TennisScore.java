package victor.testing.tdd.classic;

import org.jetbrains.annotations.NotNull;

public class TennisScore {
    private int player1Point;
    private int player2Point;

    public String getScore() {
        return translateScore(player1Point) + " - " + translateScore(player2Point);
    }

    private String translateScore(int player2Point) {
        String player2Score = "Love";
        if (player2Point == 1) {
            player2Score = "Fifteen";
        }
        return player2Score;
    }

    public void addPoint(Player player) {
        if(player== Player.ONE) {
            player1Point ++;
        } else {
            player2Point ++;
        }
    }

}
