package victor.testing.tdd.classic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {

    private final TennisScore score = new TennisScore();

    //The running score of each game is described in a manner
    // peculiar to tennis: scores from zero to three points are
    // described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.

    @Test
    void newGame() {
        String actual = score.getScore();
        assertThat(actual).isEqualTo("Love - Love");
    }
    @Test
    void fifteenLove() {
        score.wonPoint(Player.ONE);
        String actual = score.getScore();
        assertThat(actual).isEqualTo("Fifteen - Love");
    }
}
