package ro.victor.unittest.tdd.tennis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TennisGameParameterizedTest {

    private final int scorePlayer1;
    private final int scorePlayer2;
    private final String expectedScoreString;

    public TennisGameParameterizedTest(int scorePlayer1, int scorePlayer2, String expectedScoreString) {
        this.scorePlayer1 = scorePlayer1;
        this.scorePlayer2 = scorePlayer2;
        this.expectedScoreString = expectedScoreString;
    }

    @Parameterized.Parameters(name = "For points {0}-{1}: score should be {2}")
    public static List<Object[]> getParameters() {
        return Arrays.asList(
                new Object[]{0, 0, "Love - Love"},
                new Object[]{0, 1, "Love - Fifteen"},
                new Object[]{1, 0, "Fifteen - Love"},
                new Object[]{1, 1, "Fifteen - Fifteen"},
                new Object[]{17, 16, "Advantage Player1"},
                new Object[]{16, 16, "Deuce"}
        );
    }


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
    public void zaTest(){
        assertEquals(expectedScoreString, translateScore(scorePlayer1,scorePlayer2));
    }
}
