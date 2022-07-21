package victor.testing.tdd.classic;

import org.jetbrains.annotations.NotNull;

public class TennisScore {
    private int player1Point;
    private int player2Point;

    public String getScore() {
        if (player1Point >= 3 && player2Point >= 3 && player1Point == player2Point) {
            return "Deuce";
        }
        return translateScore(player1Point) + " - " + translateScore(player2Point);
    }

    private static final String[] LABELS = {"Love", "Fifteen", "Thirty", "Forty"};

    private String translateScore(int points) {
        return LABELS[points];
    }

    public void addPoint(Player player) {
        if(player== Player.ONE) {
            player1Point ++;
        } else {
            player2Point ++;
        }
    }

}
