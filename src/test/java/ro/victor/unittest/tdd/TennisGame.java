package ro.victor.unittest.tdd;

public class TennisGame {
    private static final String[] labels = {"Love", "Fifteen", "Thirty", "Forty"};

    private String score = "Love Love";
    private int player1Points;
    private int player2Points;

    public String score() {
        if (player1Points >= 3 && player2Points >= 3) {
            if (player1Points == player2Points) {
                return "Deuce";
            }
            if (player1Points - player2Points == 1) {
                return "Advantage Player1";
            }
            if (player2Points - player1Points == 1) {
                return "Advantage Player2";
            }
        }
        if (player1Points >= 4 && player1Points-player2Points >= 2) {
            return "Game Won Player1";
        }
        if (player2Points >= 4 && player2Points-player1Points >= 2) {
            return "Game Won Player2";
        }
        return labels[player1Points] + " " + labels[player2Points];
    }

    public void point(int playerNo) {
        if (playerNo == 1) {
            player1Points ++;
//            score = "Fifteen Love";
        } else {
            player2Points ++;
//            score = "Love Fifteen";
        }
    }
}
