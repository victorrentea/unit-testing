package victor.testing.tdd.classic;

public class TennisScore {
    private static String firstPlayerScore = "Love";
    private static String secondPlayerScore = "Love";

    public static String getScore() {
        return firstPlayerScore + ":" + secondPlayerScore;
    }

    public static void addPoint(Player player) {
        if (player==Player.ONE) {
            firstPlayerScore = "Fifteen";
        } else {
            secondPlayerScore = "Fifteen";
        }
    }

    // Simon says... anticipating this implem funct > DON'T TEST IMPLEM FUNCTIONS.
    // TEST AT THE PUBLIC API ONLY
//     String translate(int points) {}

    // the class "Player". do we need it NOW?
    // 💋 KISS principle : don't do it now

    // only alowed to change prod if a test is failing/not compiling
}