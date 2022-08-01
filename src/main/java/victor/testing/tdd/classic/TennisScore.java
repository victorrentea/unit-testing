package victor.testing.tdd.classic;

public class TennisScore {

    private String string = "Love - Love";


    public String getScore() {
        return string;
    }

    public void wonPoint(Player player) {
        string = "Fifteen - Love";
    }
}
