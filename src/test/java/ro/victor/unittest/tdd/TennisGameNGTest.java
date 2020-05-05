package ro.victor.unittest.tdd;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class TennisGameNGTest {


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

    @Test(dataProvider = "scores")
    public void zaTest(int player1Score, int player2Score, String expectedStr) {
        assertEquals(expectedStr, getScoreString(player1Score, player2Score));
    }

    @DataProvider
    public Iterator<Object[]> scores() {
        return asList(
                new Object[]{0,0,"Love-Love"},
                new Object[]{7,9,"Game won Player2"}
        ).iterator();
    }


}
