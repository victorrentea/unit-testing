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
    @Test
    void loveForty() {
        tennis.wonPoint(Player.TWO);
        tennis.wonPoint(Player.TWO);
        tennis.wonPoint(Player.TWO);
        String actual = tennis.getScore();
        assertThat(actual).isEqualTo("Love - Forty");
    }

    //If at least three points have been scored
    // by each player, and the scores are equal,
    // the score is “Deuce”.

    @Test
    void deuce() {
        wonPoints(Player.ONE, 3);
        wonPoints(Player.TWO, 3);
        String actual = tennis.getScore();

        assertThat(actual).isEqualTo("Deuce");
    }
    @Test
    void deuce4() {
        wonPoints(Player.ONE, 4);
        wonPoints(Player.TWO, 4);
        String actual = tennis.getScore();

        assertThat(actual).isEqualTo("Deuce");
    }
    @Test
    void advantagePlayer1() {
        wonPoints(Player.ONE, 14);
        wonPoints(Player.TWO, 13);
        String actual = tennis.getScore();

        assertThat(actual).isEqualTo("Advantage Player1");
    }

    // bebe mic testing framework....
    private void wonPoints(Player player, int points) {
        for (int i = 0; i < points; i++) {
            tennis.wonPoint(player);
        }
    }
}
