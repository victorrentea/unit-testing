package victor.testing.tdd.classic;

public class TennisScore {

    private String message = "Love - Love";
//    int player1Score;
//    int player2Score;

    public String getScore() {
        return message;
    }

    public void winPoint(int playerIndex) {
        if (message.equals("Fifteen - Love")) {
            message = "Thirty - Love";
        } else
        message = "Fifteen - Love";
    }
    // messageByScore
}
