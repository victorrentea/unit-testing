package victor.testing.tdd.classic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

public class TennisScoreParameterizedTest {

    private final TennisScore tennisScore = new TennisScore();

    public static List<Arguments> datele() {
        return List.of(
                of(0, 0, "Love - Love"),
                of(0, 1, "Love - Fifteen"),
                of(13, 12, "Advantage Player1"),
                of(0, 3, "Love - Forty")
        );
    }

    @ParameterizedTest(name = "Scor pentru jucator1={0} si jucator2={1} este {2}")
    @MethodSource("datele")
    void unu(int player1Points, int player2Points, String expectedScore) {
        setPoints(player1Points, player2Points);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo(expectedScore);
    }

    private void setPoints(int player1Points, int player2Points) {
        for (int i = 0; i < player1Points; i++) {
            tennisScore.winPoint(0);
        }
        for (int i = 0; i < player2Points; i++) {
            tennisScore.winPoint(1);
        }
    }

}
