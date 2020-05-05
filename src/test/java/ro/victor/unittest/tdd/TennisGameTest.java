package ro.victor.unittest.tdd;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {


    private String getScoreString(int player1Score, int player2Score) {
        TennisGame tennisGame = new TennisGame();
        setScore(tennisGame, player1Score, player2Score);
        return tennisGame.score();
    }

    private void setScore(TennisGame tennisGame, int player1Score, int player2Score) {
        setPlayerScore(tennisGame, 1, player1Score);
        setPlayerScore(tennisGame, 2, player2Score);
    }

    private void setPlayerScore(TennisGame tennisGame, int playerNumber, int playerScore) {
        for (int i = 0; i < playerScore; i++) {
            tennisGame.addPoint(playerNumber);
        }
    }

    @Test
    public void loveLove() {
        assertEquals("Love-Love", getScoreString(0, 0));
    }

    @Test
    public void loveFifteen() {
        assertEquals("Love-Fifteen", getScoreString(0, 1));
    }

    @Test
    public void fifteenLove() {
        assertEquals("Fifteen-Love", getScoreString(1, 0));
    }

    @Test
    public void thirtyLove() {
        assertEquals("Thirty-Love", getScoreString(2, 0));
    }
//    @Test(expected = IllegalStateException.class)
//    public void aberrant() {
//        setScore(7, 0);
//        tennisGame.score();
//    }

    @Test
    public void fortyLove() {
        assertEquals("Forty-Love", getScoreString(3, 0));
    }

    @Test
    public void deuce() {
        assertEquals("Deuce", getScoreString(3, 3));
    }
    @Test
    public void deuce4() {
        assertEquals("Deuce", getScoreString(4, 4));
    }

    @Test
    public void advantagePlayer1() {
        assertEquals("Advantage Player1", getScoreString(7, 6));
    }

    @Test
    public void advantagePlayer2() {
        assertEquals("Advantage Player2", getScoreString(3, 4));
    }

    @Test
    public void mar() {
        assertEquals("Game won Player1", getScoreString(4, 0));
    }

    @Test
    public void mar2() {
        assertEquals("Game won Player1", getScoreString(7, 5));
    }

    @Test
    public void player2Won() {
        assertEquals("Game won Player2", getScoreString(0, 4));
    }




}
