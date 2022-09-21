package victor.testing.tdd.classic;

import org.junit.jupiter.api.*;
import victor.testing.tools.ReplaceCamelCase;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(ReplaceCamelCase.class)
//@TestMethodOrder(MethodOrderer.class) // don't!
public class TennisScoreShould {

//     #2 - only ONE instance of the obj / shared by all tests - WRONG!
    // Junit instantiates a NEW test class / each @Test =>
    private final TennisScore tennisScore = new TennisScore();

    @BeforeEach
    final void before() {
//        // #1 - before each test
//        tennisScore = new TennisScore();
        System.out.println(tennisScore);
    }

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
        tennisScore.winsPoint(Player.TWO);
        String score = tennisScore.getScore();
        assertThat(score).isEqualTo("Love - Fifteen");
    }
}
