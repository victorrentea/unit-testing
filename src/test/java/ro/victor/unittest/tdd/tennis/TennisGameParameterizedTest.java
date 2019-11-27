package ro.victor.unittest.tdd.tennis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TennisGameParameterizedTest {
    private final int player1Score;
    private final int player2Score;
    private final String expectedScoreString;

    public TennisGameParameterizedTest(int player1Score, int player2Score, String expectedScoreString) {
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.expectedScoreString = expectedScoreString;
    }
    @Parameterized.Parameters(name = "Score {0} - {1} = {2}")
    public static List<Object[]> x() {
        return Arrays.asList(
                new Object[]{0,0,"Love - Love"},
                new Object[]{1,0,"Fifteen - Love"},
                new Object[]{6,7,"Advantage Player 2"},
                new Object[]{32,30,"Win Player 1"}
        );
    }

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
    public void uniculSiInegalabilulSiAdevaratulTest() {
        assertEquals(expectedScoreString, formatScore(player1Score, player2Score));
    }
    @Test
    public void dinNouCaCineStie() {
        assertEquals(expectedScoreString, formatScore(player1Score, player2Score));
    }



}
