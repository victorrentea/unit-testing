package victor.testing.tdd.classic;

public class TennisScore {

    private int player1Score;
    private int player2Score;

    public String getScore() {
        return scoreAsText(player1Score) + " - " + scoreAsText(player2Score);
    }

//    private static final String[] cStyle = {"Love", "Fifteen", "Thirty", "Forty"};
    private String scoreAsText(int score) {
//        return cStyle[score];
        switch (score) {
            case 0:
                return "Love";
            case 1:
                return "Fifteen";
            case 2:
                return "Thirty";
            default:
                return "Forty";
        }
        // uite ce vine in java 17 frate
//        return switch (score) {
//            case 0 -> "Love";
//            case 1 -> "Fifteen";
//            case 2 -> "Thirty";
//            default -> "Forty";
//        };
     }

    public void winPoint(int playerIndex) {
        if (playerIndex == 0) {
            player1Score ++;
        } else {
            player2Score ++;
        }
    }
}
