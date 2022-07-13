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
        tennisScore.winsPoints(Player.ONE);
        tennisScore.winsPoints(Player.TWO);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Fifteen - Fifteen");
    }
    @Test
    void thirtyLove() {
        tennisScore.winsPoints(Player.ONE);
        tennisScore.winsPoints(Player.ONE);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Thirty - Love");

    }
    @Test
    void fortyLove() {
//        tennisScore.winsPoints(Player.ONE);
//        tennisScore.winsPoints(Player.ONE);
//        tennisScore.winsPoints(Player.ONE);
        addPoints(3, 0);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Forty - Love");
    }

    //If at least three points have been scored by each player,
    // and the scores are equal, the score is “Deuce”.
    @Test
    void deuce() {
//        tennisScore.winsPoints(Player.ONE); // REST API CALL1
//        tennisScore.winsPoints(Player.ONE);
//        tennisScore.winsPoints(Player.ONE);
//
//        tennisScore.winsPoints(Player.TWO, 3);
//        tennisScore.winsPoints(Player.TWO);
//        tennisScore.winsPoints(Player.TWO);
        addPoints(3,3);

        String actual = tennisScore.getScore(); // REST API CALL 2

        assertThat(actual).isEqualTo("Deuce");
    }
    @Test
    void deuce4() {
        // unfriendly tested API or more complexity inside the bubble:
        // a) change the prod API ! IF IT MAKES SENSE. here it does NOT make sense
        // b) build youself a mini-testing helpers... framework
//        tennisScore.winsPoints(Player.ONE);
//        tennisScore.winsPoints(Player.ONE);
//        tennisScore.winsPoints(Player.ONE);
//        tennisScore.winsPoints(Player.ONE);
//
//        tennisScore.winsPoints(Player.TWO);
//        tennisScore.winsPoints(Player.TWO);
//        tennisScore.winsPoints(Player.TWO);
//        tennisScore.winsPoints(Player.TWO);
        addPoints(4, 4);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Deuce");
    }

    private void addPoints(int player1, int player2) {
        addPointsToPlayer(player1, Player.ONE);
        addPointsToPlayer(player2, Player.TWO);
    }

    private void addPointsToPlayer(int player2, Player two) {
        for (int i = 0; i < player2; i++) {
            tennisScore.winsPoints(two);
        }
    }
}
