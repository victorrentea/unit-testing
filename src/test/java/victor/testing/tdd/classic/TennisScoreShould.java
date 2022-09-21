package victor.testing.tdd.classic;

import org.junit.jupiter.api.*;
import victor.testing.mutation.Customer;
import victor.testing.tools.ReplaceCamelCase;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(ReplaceCamelCase.class)
//@TestMethodOrder(MethodOrderer.class) // don't!
public class TennisScoreShould {

    // Junit instantiates a NEW test class / each @Test =>
    private final TennisScore tennisScore = new TennisScore();


    @Test
    void returnsLoveLoveForNewGame() {
        String score = tennisScore.getScore();
        assertThat(score).isEqualTo("Love - Love");
    }

    //The running score of each game is described in a manner
    // peculiar to tennis: scores from zero to three points
    // are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.

    @Test
    void returnsFifteenLoveWhenPlayer1Scores1Point() {
        tennisScore.winsPoint(Player.ONE);

        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Fifteen - Love");
    }

    @Test
    void returnsLoveFifteenWhenPlayer2Scores1Point() {
        // given
        tennisScore.winsPoint(Player.TWO);

        // when - prod call
        String score = tennisScore.getScore();

        // then : assert + verify
        assertThat(score).isEqualTo("Love - Fifteen");
    }

    @Test
    void returnsThirtyLoveWhenPlayer1Scores2Points() {
        tennisScore.winsPoint(Player.ONE);
        tennisScore.winsPoint(Player.ONE);

        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Thirty - Love");
    }

    @Test
    void returnsFortyLoveWhenPlayer1Scores3Points() {
        tennisScore.winsPoint(Player.ONE);
        tennisScore.winsPoint(Player.ONE);
        tennisScore.winsPoint(Player.ONE);

        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Forty - Love");
    }
    @Test
    void returnsDeuce() {
        tennisScore.winsPoint(Player.ONE);
        tennisScore.winsPoint(Player.ONE);
        tennisScore.winsPoint(Player.ONE);
        tennisScore.winsPoint(Player.TWO);
        tennisScore.winsPoint(Player.TWO);
        tennisScore.winsPoint(Player.TWO);

        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Deuce");
    }
}
