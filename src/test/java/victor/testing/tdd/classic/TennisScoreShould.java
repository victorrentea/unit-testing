package victor.testing.tdd.classic;

import org.junit.jupiter.api.*;
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
    void fifteenFifteen() {
        // test overlap = you can't make this test FAIL alone, no matter how you mess the rpod
        tennisScore.winsPoint(Player.ONE);
        tennisScore.winsPoint(Player.TWO);

        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Fifteen - Fifteen");
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
        addMorePoints(Player.ONE, 3);

        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Forty - Love");
    }

    @Test
    void returnsDeuce() {
        // Option B: change PROD tested API to make it easier to test. add #points as param
        // sometimes doesn't make sense to change prod API like this.
        // eg. prod only call winsPoint(.., 1) > DON'T DO THIS refactoring
        //        tennisScore.winsPoint(Player.ONE);
        //        tennisScore.winsPoint(Player.TWO);

        // Option A:
        // test helper methods, tomorrow a mini testing-framework >>> even larger
        // RISK: the helper will HIDE perhaps surprises.
        // RISK: the test helper can contain BUGS
        // CHALLENGE: evolve it with time
        addMorePoints(Player.ONE, 3);
        addMorePoints(Player.TWO, 3);

        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Deuce");
    }
    @Test
    void returnsDeuce4() {
        addMorePoints(Player.ONE, 4);
        addMorePoints(Player.TWO, 4);

        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Deuce");
    }

    private void addMorePoints(Player player, int n) {
        for (int i = 0; i < n; i++) {
            tennisScore.winsPoint(player);
        }
    }
}
