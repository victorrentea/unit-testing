package victor.testing.tdd.classic;

import java.util.HashMap;
import java.util.Map;

public class TennisScore {

    private int player1Points;
    private int player2Points;
    private static final Map<Integer, String> POINTS_TO_SCORE = Map.of(
            0, "Love",
            1, "Fifteen",
            2, "Thirty",
            3, "Forty"
    );

    public String getScore() {
        if (player1Points >= 3 && player2Points >= 3) {
            if (player1Points == player2Points + 1) {
                return "Advantage Player1";
            }
            if (player1Points == player2Points) {
                return "Deuce";
            }
        }
        return translate(player1Points) + " - " + translate(player2Points);
        }

    private String translate(int player1Points) {
        return POINTS_TO_SCORE.get(player1Points);
    }

    public void wonPoint(Player player) {
        if (player == Player.ONE) {
            player1Points ++;
        }else {
            player2Points ++;
        }
    }
}
