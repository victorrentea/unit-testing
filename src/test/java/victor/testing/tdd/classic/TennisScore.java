package victor.testing.tdd.classic;

import java.util.Objects;

public class TennisScore {
    private String firstPlayerScore = "Love";
    private String secondPlayerScore = "Love";

    public String getScore() {
        return firstPlayerScore + ":" + secondPlayerScore;
    }

    public void addPoint(Player player) {
        if (player==Player.ONE) {
            firstPlayerScore = "Fifteen";
        } else {
            if (Objects.equals(secondPlayerScore, "Fifteen"))
                secondPlayerScore = "Thirty";
            else secondPlayerScore = "Fifteen";
        }
    }

    // Simon says... anticipating this implem funct > DON'T TEST IMPLEM FUNCTIONS.
    // TEST AT THE PUBLIC API ONLY
//     String translate(int points) {}

    // the class "Player". do we need it NOW?
    // 💋 KISS principle : don't do it now

    // only alowed to change prod if a test is failing/not compiling
}
