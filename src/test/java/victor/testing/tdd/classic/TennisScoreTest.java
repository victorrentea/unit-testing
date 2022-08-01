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
        iaoPasta(3, 3, "Deuce");
    }
    @Test
    void deuce4() {
        iaoPasta(4, 4, "Deuce");
    }
    @Test
    void advantagePlayer1() {

        iaoPasta(14, 13, "Advantage Player1");
    }

    private void iaoPasta(int points1, int points2, String expectedScore) {
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
