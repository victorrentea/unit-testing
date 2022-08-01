package victor.testing.tdd.classic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {

    private final TennisScore tennis = new TennisScore();

    //The running score of each game is described in a manner
    // peculiar to tennis: scores from zero to three points are
    // described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.

    @Test
    void newGame() {
        String actual = tennis.getScore();
        assertThat(actual).isEqualTo("Love - Love");
    }

    @Test
    void fifteenLove() {
        tennis.wonPoint(Player.ONE);
        String actual = tennis.getScore();
        assertThat(actual).isEqualTo("Fifteen - Love");
    }
    @Test
    void thirtyLove() {
        tennis.wonPoint(Player.ONE);
        tennis.wonPoint(Player.ONE);
        String actual = tennis.getScore();
        assertThat(actual).isEqualTo("Thirty - Love");
    }
    @Test
    void loveFifteen() {
        tennis.wonPoint(Player.TWO);
        String actual = tennis.getScore();
        assertThat(actual).isEqualTo("Love - Fifteen");
    }


}
