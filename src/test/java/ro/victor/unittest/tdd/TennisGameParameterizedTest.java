package ro.victor.unittest.tdd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class TennisGameParameterizedTest {

    private final Parameter parameter;

    private static class Parameter {
        private final int player1Points;
        private final int player2Points;
        private final String expectedScoredString;

        private Parameter(int player1Points, int player2Points, String expectedScoredString) {
            this.player1Points = player1Points;
            this.player2Points = player2Points;
            this.expectedScoredString = expectedScoredString;
        }

        @Override
        public String toString() {
            return "player1:" + player1Points +
                    ", player2:" + player2Points +
                    ", score:" + expectedScoredString;
        };
    }

    public TennisGameParameterizedTest(Parameter parameter) {
        this.parameter = parameter;
    }

    @Parameterized.Parameters(name ="{0}")
    public static List<Parameter> testData() {
        return Arrays.asList(
          new Parameter(0,0,"Love Love"),
          new Parameter(0,1,"Love Fifteen"),
          new Parameter(5,5,"Deuce")
        );
    }

    private String scoreForPoints(int player1Points, int player2Points) {
        TennisGame tennisGame = new TennisGame();
        setPointsForPlayer(tennisGame, 1, player1Points);
        setPointsForPlayer(tennisGame, 2, player2Points);
        return tennisGame.score();
    }

    private void setPointsForPlayer(TennisGame tennisGame, int playerNo, int points) {
        for (int i = 0; i < points; i++) {
            tennisGame.point(playerNo);
        }
    }

    @Test
    public void theTest() {
        assertThat(scoreForPoints(parameter.player1Points,parameter.player2Points)).isEqualTo(parameter.expectedScoredString);
    }

}
