package victor.testing.tdd.classic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGamePureTest {


    @Test
    public void loveLove() {
        assertEquals("Love-Love", getScore(0, 0));
    }

    @Test
    public void loveFifteen() {
        assertEquals("Love-Fifteen", getScore(0, 1));
    }

    @Test
    public void fifteenLove() {
        assertEquals("Fifteen-Love", getScore(1, 0));
    }

    @Test
    public void thirtyLove() {
        assertEquals("Thirty-Love", getScore(2, 0));
    }

    @Test
    public void fortyLove() {
        assertEquals("Forty-Love", getScore(3, 0));
    }

    @Test
    public void deuce() {
        assertEquals("Deuce", getScore(3, 3));
    }

    @Test
    public void advantagePlayer1() {
        assertEquals("Advantage Player1", getScore(7, 6));
    }

    @Test
    public void advantagePlayer2() {
        assertEquals("Advantage Player2", getScore(3, 4));
    }

    @Test
    public void mar() {
        assertEquals("Game won Player1", getScore(4, 0));
    }

    @Test
    public void mar2() {
        assertEquals("Game won Player1", getScore(7, 5));
    }

    @Test
    public void player2Won() {
        assertEquals("Game won Player2", getScore(0, 4));
    }



    private String getScore(int player1Score, int player2Score) {
        TennisGame tennisGame = new TennisGame();
        setPlayerScore(1, player1Score, tennisGame);
        setPlayerScore(2, player2Score, tennisGame);
        return tennisGame.score();
    }

    private void setPlayerScore(int playerNumber, int playerScore, TennisGame tennisGame) {
        for (int i = 0; i < playerScore; i++) {
            tennisGame.addPoint(playerNumber);
        }
    }
}
