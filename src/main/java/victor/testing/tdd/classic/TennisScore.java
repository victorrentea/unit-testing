package victor.testing.tdd.classic;

public class TennisScore {

    private static String message = "Love - Love";

    public static String getScore() {
        return message;
    }

    public static void winPoint(int playerIndex) {
        message = "Fifteen - Love";
    }
}
