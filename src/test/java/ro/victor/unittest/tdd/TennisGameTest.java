package ro.victor.unittest.tdd;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {
    private TennisGame tennisGame = new TennisGame();


    private void setScore(int player1Score, int player2Score) {
        setPlayerScore(1, player1Score);
        setPlayerScore(2, player2Score);
    }

    private void setPlayerScore(int playerNumber, int playerScore) {
        for (int i = 0; i < playerScore; i++) {
            tennisGame.addPoint(playerNumber);
        }
    }

    @Test
    public void loveLove() {
        setScore(0, 0);
        String score = tennisGame.score();
        assertEquals("Love-Love", score);
    }

    @Test
    public void loveFifteen() {
        setScore(0, 1);
        String score = tennisGame.score();
        assertEquals("Love-Fifteen", score);
    }

    @Test
    public void fifteenLove() {
        setScore(1, 0);
        String score = tennisGame.score();
        assertEquals("Fifteen-Love", score);
    }

    @Test
    public void thirtyLove() {
        setScore(2, 0);
        String score = tennisGame.score();
        assertEquals("Thirty-Love", score);
    }
//    @Test(expected = IllegalStateException.class)
//    public void aberrant() {
//        setScore(7, 0);
//        tennisGame.score();
//    }

    @Test
    public void fortyLove() {
        setScore(3, 0);
        String score = tennisGame.score();
        assertEquals("Forty-Love", score);
    }

    @Test
    public void deuce() {
        setScore(3, 3);
        String score = tennisGame.score();
        assertEquals("Deuce", score);
    }
    @Test
    public void deuce4() {
        setScore(4, 4);
        String score = tennisGame.score();
        assertEquals("Deuce", score);
    }

    @Test
    public void advantagePlayer1() {
        setScore(7, 6);
        String score = tennisGame.score();
        assertEquals("Advantage Player1", score);
    }

    @Test
    public void advantagePlayer2() {
        setScore(3, 4);
        String score = tennisGame.score();
        assertEquals("Advantage Player2", score);
    }

    @Test
    public void mar() {
        setScore(4, 0);
        String score = tennisGame.score();
        assertEquals("Game won Player1", score);
    }

    @Test
    public void mar2() {
        setScore(7, 5);
        String score = tennisGame.score();
        assertEquals("Game won Player1", score);
    }

    @Test
    public void player2Won() {
        setScore(0, 4);
        String score = tennisGame.score();
        assertEquals("Game won Player2", score);
    }




}
