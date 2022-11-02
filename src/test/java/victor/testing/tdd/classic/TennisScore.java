package victor.testing.tdd.classic;

import java.util.Objects;

public class TennisScore {
    private int player1Points = 0;
    private int player2Points = 0;
//    private String firstPlayerScore = "Love";
//    private String secondPlayerScore = "Love";

    public String getScore() {
        // what here ????
//        return firstPlayerScore + ":" + secondPlayerScore;
        return translate(player1Points) + ":" + translate(player2Points);
    }

    public void addPoint(Player player) {
        if (player==Player.ONE) {
//            firstPlayerScore = "Fifteen";
            player1Points++;
        } else {
            player2Points++;
//            if (Objects.equals(secondPlayerScore, "Fifteen"))
//                secondPlayerScore = "Thirty";
//            else secondPlayerScore = "Fifteen";
        }
    }

    // Simon says... anticipating this implem funct > DON'T TEST IMPLEM FUNCTIONS.
    // TEST AT THE PUBLIC API ONLY
    private String translate(int points) {
        if (points == 0) {
            return "Love";
        } else if (points == 1) {
            return "Fifteen";
        } else if (points == 2) {
            return "Thirty";
        }
        throw new IllegalArgumentException("impossible");
    }

    // the class "Player". do we need it NOW?
    // 💋 KISS principle : don't do it now

    // only alowed to change prod if a test is failing/not compiling
}
