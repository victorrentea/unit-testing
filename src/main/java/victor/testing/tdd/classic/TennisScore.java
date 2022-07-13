package victor.testing.tdd.classic;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TennisScore {
    private int player1Point;
    private int player2Point;

    public String getScore() {
        if (player1Point == player2Point && player1Point >= 3 && player2Point >= 3) {
            return "Deuce";
        }
        return translateScore(player1Point) + " - " + translateScore(player2Point);
    }

    private String translateScore(int player1Point) {
        switch (player1Point) {
            case 0:
                return "Love";
            case 1:
                return "Fifteen";
            case 2:
                return "Thirty";
            case 3:
                return "Forty";
            default:
                throw new IllegalStateException("Unexpected value: " + player1Point);
        }
    }

    public void winsPoints(Player player) {
        if (player == Player.ONE) {
            player1Point++;
        } else {
            player2Point++;
        }

    }
}
