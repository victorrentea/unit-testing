package ro.victor.unittest.tdd.tennis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {

    private String formatScore(int score1, int score2) {
        TennisGame tennisGame = new TennisGame();
        setScoreForPlayer(tennisGame, 1, score1);
        setScoreForPlayer(tennisGame, 2, score2);

        return tennisGame.score();
    }

    private void setScoreForPlayer(TennisGame tennisGame, int playerNumber, int score) {
        for (int i = 0; i < score; i++) {
            tennisGame.playerScores(playerNumber);
        }
    }

    @Test
    public void initialScore() {
        assertEquals("Love - Love", formatScore(0, 0));
    }

    @Test
    public void scoreLoveFifteen() {
        assertEquals("Love - Fifteen", formatScore(0, 1));
    }

    @Test
    public void scoreFifteenLove() {
        assertEquals("Fifteen - Love", formatScore(1, 0));
    }

    @Test
    public void scoreFifteenFifteen() {
        assertEquals("Fifteen - Fifteen", formatScore(1, 1));
    }

    @Test
    public void scoreLoveThirty() {
        assertEquals("Love - Thirty", formatScore(0, 2));
    }

    @Test
    public void scoreLoveForty() {
        assertEquals("Love - Forty", formatScore(0, 3));
    }

    @Test
    public void scoreDeuce() {
        assertEquals("Deuce", formatScore(3, 3));
    }

    @Test
    public void scoreDeuce2() {
        assertEquals("Deuce", formatScore(4, 4));
    }

    @Test
    public void scoreAdvantage1() {
        assertEquals("Advantage Player 1", formatScore(4, 3));
    }

    @Test
    public void scoreAdvantage2() {
        assertEquals("Advantage Player 2", formatScore(3, 4));
    }

    @Test
    public void scoreWinPlayer1() {
        assertEquals("Win Player 1", formatScore(4, 0));
    }

    @Test
    public void scoreWinPlayer2() {
        assertEquals("Win Player 2", formatScore(0, 4));
    }


}
