package victor.testing.tdd.classic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreParamterized {
    private final TennisScore tennisScore = new TennisScore();


    @ParameterizedTest(name = "Score for points {0},{1} is {2}\"")
    @CsvSource({
            "0, 0, Love - Love",
            "0, 1, Love - Fifteen",
            "0, 2, Love - Thirty",
            "0, 3, Love - Forty",
            "0, 3, Love - Forty",
            "0, 3, Love - Forty",
            "0, 3, Love - Forty",
    })
    void oneSingleTest(int player1Points, int player2Points, String expectedScoreString) {
        addMorePoints(Player.ONE, player1Points);
        addMorePoints(Player.TWO, player2Points);

        String score = tennisScore.getScore();

        assertThat(score).isEqualTo(expectedScoreString);
    }

    private void addMorePoints(Player player, int n) {
        for (int i = 0; i < n; i++) {
            tennisScore.winsPoint(player);
        }
    }
}
