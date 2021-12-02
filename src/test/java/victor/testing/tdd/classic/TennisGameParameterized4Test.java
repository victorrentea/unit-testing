package victor.testing.tdd.classic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class TennisGameParameterized4Test {

    private final int player1Score;
    private final int player2Score;
    private final String expectedScoreString;

    @Parameterized.Parameters(name="Score for {0}-{1} is \"{2}\"")
    public static List<Object[]> date() {
        return Arrays.asList(
                new Object[]{0, 0, "Love-Love"},
                new Object[]{0, 1, "Love-Fifteen"},
                new Object[]{0, 2, "Love-Thirty"},
                new Object[]{0, 3, "Love-Forty"},
                new Object[]{0, 4, "Game won Player2"},
                new Object[]{4, 4, "Deuce"},
                new Object[]{1, 1, "Fifteen-Fifteen"}
        );
    }

    public TennisGameParameterized4Test(int player1Score, int player2Score, String expectedScoreString) {
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.expectedScoreString = expectedScoreString;
    }


    @Test
    public void theTest() {
        assertEquals(expectedScoreString, getScore(player1Score, player2Score));
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
