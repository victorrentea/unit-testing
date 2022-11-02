package victor.testing.tdd.classic;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
    // down-clocking your brain: when developing with TDD you might BLOCK: your brain goes flat / req fuzzy
    // TDD tells you to feature-slice the requ to the littlest amount of behavior to test

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
    @Test
    void loveForty() {
        tennisScore.addPoint(Player.TWO);
        tennisScore.addPoint(Player.TWO);
        tennisScore.addPoint(Player.TWO);
        String actual = tennisScore.getScore();
        assertThat(actual).isEqualTo("Love:Forty");
    }
    @Test// redundant/overlapping test
    // in TDD when a new test is already green
    // - stupid your test is INCORRECT
    // - stupid accidental overlap believe this part of the feature was NOT eyy implment
    // - ME: smart if you TRANSLATE 1:1 the examples in the spec to tests.
    //      "For example: Exa1, Exa2... " in a feature description/spec
    void fortyLove() {
        tennisScore.addPoint(Player.ONE);
        tennisScore.addPoint(Player.ONE);
        tennisScore.addPoint(Player.ONE);
        String actual = tennisScore.getScore();
        assertThat(actual).isEqualTo("Forty:Love");
    }

    @Test
    void deuce() {
        tennisScore.addPoint(Player.ONE);
        tennisScore.addPoint(Player.ONE);
        tennisScore.addPoint(Player.ONE);
        tennisScore.addPoint(Player.TWO);
        tennisScore.addPoint(Player.TWO);
        tennisScore.addPoint(Player.TWO);
        String actual = tennisScore.getScore();
        assertThat(actual).isEqualTo("Deuce");
    }
    @Test
    void deuce4() {
        tennisScore.addPoint(Player.ONE);
        tennisScore.addPoint(Player.ONE);
        tennisScore.addPoint(Player.ONE);
        tennisScore.addPoint(Player.ONE); // YES We Enjoy Typing = WET <> DRY

        tennisScore.addPoint(Player.TWO);
        tennisScore.addPoint(Player.TWO);
        tennisScore.addPoint(Player.TWO);
        tennisScore.addPoint(Player.TWO);
        String actual = tennisScore.getScore();
        assertThat(actual).isEqualTo("Deuce");
    }



}
