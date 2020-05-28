package ro.victor.unittest.tdd.tennis;

import org.apache.commons.lang3.NotImplementedException;

public class TennisGame {
    public static final String[] SCORES = {"Love", "Fifteen", "Thirty", "Forty"};
    private int player1Score;
    private int player2Score;
    private String winner = "";

    public String getWinner() {
        return winner;
    }

    public String score() {
        if (player1Score >= 4 && player1Score - player2Score >= 2) {
//            winner += "Player1";
            return "Game won Player1";
        }
        else if (player2Score >= 4 && player2Score - player1Score >= 2) {
//            winner += "Player2";
            return "Game won Player2";
        }
        if (player1Score >= 3 && player2Score >= 3) {
            if (player1Score == player2Score) {
                return "Deuce";
            } else if (player1Score == player2Score + 1) {
                return "Advantage Player1";
            } else if (player2Score == player1Score + 1) {
                return "Advantage Player2";
            } /*else {
                throw new NotImplementedException("...");
            }*/
            return "NOT IMPLEMENTED FRATE";
        } else {
            return SCORES[player1Score] + "-" + SCORES[player2Score];
        }

        // Line coverage zice doar ca ai trecut prin acea linie

        // if (ceva) {}  //fara else
        // Branch coverage ti-ar da 50% pentru un IF in care intrii doar pe "then"
        // Testul nu a vazut caz in care tu NU intri in if pentru ca era conditia false
    }

    public void addPoint(int playerNumber) {
        if (playerNumber == 1) {
            player1Score++;
        } else {
            player2Score++;
        }
    }
}
