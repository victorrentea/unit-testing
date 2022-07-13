package victor.testing.tdd.classic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {

    TennisScore tennisScore = new TennisScore();

    // The running score of each game is described in a manner peculiar to tennis:
    // scores from zero to three points are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
    @Test
    void loveLove() {
        String actual = tennisScore.getScore();
        assertThat(actual).isEqualTo("Love - Love");
    }

    @Test
    void fifteenLove() {
        tennisScore.winsPoints(Player.ONE); //

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Fifteen - Love");
    }

    @Test
    void loveFifteen() {
        tennisScore.winsPoints(Player.TWO); //

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Love - Fifteen");
    }

    @Test
    void fifteenFifteen() {
        tennisScore.winsPoints(Player.ONE); //
        tennisScore.winsPoints(Player.TWO); //

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Fifteen - Fifteen");

    }
}
