package ro.victor.unittest.tdd.tennis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {


    private String translateScore(int player1Points, int player2Points) {
        TennisGame game = new TennisGame();
        addPointsForPlayer(game,1, player1Points);
        addPointsForPlayer(game, 2, player2Points);
        return game.score();
    }

    private void addPointsForPlayer(TennisGame game, int player, int points) {
        for (int i = 0; i < points; i++) {
            game.scorePoint(player);
        }
    }

    @Test
    public void returnsLoveLoveWhenNewGame(){
        assertEquals("Love - Love", translateScore(0,0));
    }

    @Test
    public void returnsLoveFifteenWhenPlayer2ScoresOnePoint() {
        assertEquals("Love - Fifteen", translateScore(0,1));
    }

    @Test
    public void returnsFifteenLoveWhenPlayer1ScoresOnePoint() {
        assertEquals("Fifteen - Love", translateScore(1,0));
    }
    @Test
    public void returnsFifteenFifteenWhenPlayer1ScoresOnePoint() {
        assertEquals("Fifteen - Fifteen", translateScore(1,1));
    }
    @Test
    public void returns30LoveWhenPlayer1ScoresTwoPoint() {
        assertEquals("Thirty - Love", translateScore(2,0));
    }
    @Test
    public void returns40LoveWhenPlayer1ScoresThreePoint() {
        assertEquals("Forty - Love", translateScore(3,0));
    }
    @Test
    public void returnsDeuce() {
        assertEquals("Deuce", translateScore(3,3));
    }

    @Test
    public void returnsDeuce2() {
        assertEquals("Deuce", translateScore(4,4));
    }

    @Test
    public void advantagePlayer1() {
        assertEquals("Advantage Player1", translateScore(4,3));
    }

    @Test
    public void advantagePlayer2() {
        assertEquals("Advantage Player2", translateScore(3,4));
    }

}
