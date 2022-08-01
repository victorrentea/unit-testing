package victor.testing.tdd.classic;

import java.util.HashMap;
import java.util.Map;

public class TennisScore {

    private int player1Points;
    private int player2Points;
    private static final Map<Integer, String> POINTS_TO_SCORE = Map.of(
            0, "Love",
            1, "Fifteen",
            2, "Thirty"
    );

    public String getScore() {
        return POINTS_TO_SCORE.get(player1Points) + " - " +POINTS_TO_SCORE.get(player2Points);
    }

    public void wonPoint(Player player) {
        if (player == Player.ONE) {
            player1Points ++;
        }else {
            player2Points ++;
        }
    }
}
