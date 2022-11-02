package victor.testing.tdd.classic;

import java.util.Objects;

public class TennisScore {
    private int player1Points = 0;
    private int player2Points = 0;

    public String getScore() {
        if (player1Points >= 3 && player1Points == player2Points) {
            return "Deuce";
        }
        return translate(player1Points) + ":" + translate(player2Points);
    }

    public void addPoint(Player player) {
        if (player==Player.ONE) {
            player1Points++;
        } else {
            player2Points++;
        }
    }

    private String translate(int points) {
        if (points == 0) {
            return "Love";
        } else if (points == 1) {
            return "Fifteen";
        } else if (points ==2){
            return "Thirty";
        } else if (points == 3 ) {
            return "Forty";
        }
        throw new IllegalArgumentException("impossible");
    }

    // Simon says... anticipating this implem funct > DON'T TEST IMPLEM FUNCTIONS.
    // TEST AT THE PUBLIC API ONLY

    // the class "Player". do we need it NOW?
    // 💋 KISS principle : don't do it now

    // only alowed to change prod if a test is failing/not compiling
}
