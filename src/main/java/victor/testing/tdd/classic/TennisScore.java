package victor.testing.tdd.classic;

public class TennisScore {

    private int player1Points;
    private int player2Points;

    public String getScore() {
        if (player1Points == player2Points &&
            player1Points >= 3) {
            return "Deuce";
        }
        return translatePoints(player1Points) + " - " + translatePoints(player2Points);
    }

    private String translatePoints(int points) {
        switch (points) {
            case 0:
                return "Love";
            case 1:
                return "Fifteen";
            case 2:
                return "Thirty";
            case 3:
                return "Forty";
            default:
                throw new IllegalArgumentException("IDN!!");
        }
    }

    public void winsPoint(Player player) {
        if (player == Player.ONE) {
            player1Points ++;
        } else {
            player2Points ++;
        }
    }
}

enum Player {
    TWO, ONE
}