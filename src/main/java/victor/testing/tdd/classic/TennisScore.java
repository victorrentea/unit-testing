package victor.testing.tdd.classic;

import java.util.Objects;

public class TennisScore {

    private String string = "Love - Love";

    private int player1Point;
    private int player2Point;

    public String getScore() {
        return string;
    }

    public void winsPoints(Player player) {
        if (player == Player.ONE) {
            string = "Fifteen - Love";
        } else {
            if (Objects.equals(string, "Fifteen - Love"))
                string = "Fifteen - Fifteen";
            else
                string = "Love - Fifteen";
        }
    }
}
