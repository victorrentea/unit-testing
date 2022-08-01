package victor.testing.tdd.classic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

public class TennisScoreParameterizedTest {

    private final TennisScore tennis = new TennisScore();


    public static Stream<Arguments> dateleDeTest() {
        return Stream.of(
                of(0, 0, "Love - Love"),
                of(0, 3, "Love - Forty"),
                of(7, 6, "Advantage Player1"),
//                of(7, 5, "Game won Player1"),
                of(6, 6, "Deuce")
        );
    }
    @ParameterizedTest(name = "Scoru pentru {0}-{1} este {2}")
    @MethodSource("dateleDeTest")
    public void testul(int points1, int points2, String expectedScore) {
        wonPoints(Player.ONE, points1);
        wonPoints(Player.TWO, points2);
        String actual = tennis.getScore();

        assertThat(actual).isEqualTo(expectedScore);
    }

    // bebe mic testing framework....
    private void wonPoints(Player player, int points) {
        for (int i = 0; i < points; i++) {
            tennis.wonPoint(player);
        }
    }
}
