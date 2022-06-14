package victor.testing.tdd.classic;

public class TennisScore {

    private String message = "Love - Love";

    public String getScore() {
        return message;
    }

    public void winPoint(int playerIndex) {
        message = "Fifteen - Love";
    }
    // messageByScore
}
