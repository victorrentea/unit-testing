package victor.testing.tdd.classic;

public class TennisScore {

    // comment in prod code outdated
    private String score = "Love - Love";

    public String getScore() {
        return score;
    }

    public void winsPoint(Player player) {
        if (player == Player.ONE)
            score = "Fifteen - Love";
        else score = "Love - Fifteen";
    }
}

enum Player {
    TWO, ONE
}