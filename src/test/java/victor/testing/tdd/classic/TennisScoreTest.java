package victor.testing.tdd.classic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
    // down-clocking your brain: when developing with TDD you might BLOCK: your brain goes flat / req fuzzy
    // TDD tells you to feature-slice the requ to the littlest amount of behavior to test

    @Test
    void loveLove() {
        String actual = TennisScore.getScore();
        assertThat(actual).isEqualTo("Love:Love");
    }
    @Test
    void fifteenLove() {
        TennisScore.addPoint(Player.ONE);
        String actual = TennisScore.getScore();
        assertThat(actual).isEqualTo("Fifteen:Love");
    }
    @Test
    void loveFifteen() {
        TennisScore.addPoint(Player.TWO);
        String actual = TennisScore.getScore();
        assertThat(actual).isEqualTo("Love:Fifteen");
    }
}
