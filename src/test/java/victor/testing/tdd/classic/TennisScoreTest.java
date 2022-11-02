package victor.testing.tdd.classic;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
    // down-clocking your brain: when developing with TDD you might BLOCK: your brain goes flat / req fuzzy
    // TDD tells you to feature-slice the requ to the littlest amount of behavior to test

    public TennisScoreTest() {
        System.out.println("new test class instance x");
    }
    TennisScore tennisScore = new TennisScore();
    @Test
    void loveLove() {
        String actual = tennisScore.getScore();
        assertThat(actual).isEqualTo("Love:Love");
    }

    @Test
    void fifteenLove() {
        tennisScore.addPoint(Player.ONE);
        String actual = tennisScore.getScore();
        assertThat(actual).isEqualTo("Fifteen:Love");
    }

    @Test
    void loveFifteen() {
        tennisScore.addPoint(Player.TWO);
        String actual = tennisScore.getScore();
        assertThat(actual).isEqualTo("Love:Fifteen");
    }

    @Test
    void loveThirty() {
        tennisScore.addPoint(Player.TWO);
        tennisScore.addPoint(Player.TWO);
        String actual = tennisScore.getScore();
        assertThat(actual).isEqualTo("Love:Thirty");
    }
}
