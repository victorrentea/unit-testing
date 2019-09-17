package ro.victor.unittest.tdd;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {
    private TennisGame tennisGame = new TennisGame();

    private String getScoreString(int player1Score, int player2Score) {
        awardPoints(Player.ONE, player1Score);
        awardPoints(Player.TWO, player2Score);
        return tennisGame.score();
    }

    private void awardPoints(Player player, int noPoints) {
        for (int i = 0; i < noPoints; i++) {
            tennisGame.point(player);
        }
    }

    @Test
    public void loveLove() {
        assertEquals("Love-Love", getScoreString(0,0));
    }
    @Test
    public void fifteenLove() {
        assertEquals("Fifteen-Love", getScoreString(1,0));
    }
    @Test
    public void loveFifteen() {
        assertEquals("Love-Fifteen", getScoreString(0,1));
    }
    @Test
    public void fifteenFifteen() {
        assertEquals("Fifteen-Fifteen", getScoreString(1,1));
    }

    @Test
    public void loveThirty() {
        assertEquals("Love-Thirty", getScoreString(0,2));
    }

    @Test
    public void loveForty() {
        assertEquals("Love-Forty", getScoreString(0,3));
    }

    @Test
    public void deuce() {
        assertEquals("Deuce",  getScoreString(3,3));
    }

    @Test
    public void deuce4() {
        assertEquals("Deuce",  getScoreString(4,4));
    }
    @Test
    public void advantage1() {
        assertEquals("Advantage Player 1",  getScoreString(5,4));
    }
    @Test
    public void advantage2() {
        assertEquals("Advantage Player 2",  getScoreString(4,5));
    }
    @Test
    public void game1() {
        assertEquals("Game Won Player 1",
                getScoreString(4,2));
    }
    @Test
    public void game2() {
        assertEquals("Game Won Player 2",
                getScoreString(2,4));
    }
}
