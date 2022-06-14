package victor.testing.tdd.classic;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class TennisScoreTest {

    private final TennisScore tennisScore = new TennisScore();

    @BeforeEach
    final void before() {

    }
    @Test
    void newGame() {
        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Love - Love");
    }

    @Test
    void fifteenLove() {
        tennisScore.winPoint(0); // cum altfel pot spune "jucatorul 1"?
        //  TODO dupa: metode separate
        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Fifteen - Love");
    }

    @Test
    void thirtyLove() {
        //given << pune din asta mai ales cand testezi mai mult de o clasa o data.
        tennisScore.winPoint(0);
        tennisScore.winPoint(0);

        //when
        String actual = tennisScore.getScore();

        // then
        assertThat(actual).isEqualTo("Thirty - Love");
    }

    @Test
    void loveFifteen() {
        tennisScore.winPoint(1);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Love - Fifteen");
    }

    @Test
    void loveForty() {
        tennisScore.winPoint(1);
        tennisScore.winPoint(1);
        tennisScore.winPoint(1);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Love - Forty");
    }

   //  If at least three points have been scored by each player,
    //  and the scores are equal, the score is “Deuce”.

    @Test
    void deuce3_3() {
        tennisScore.winPoint(0);
        tennisScore.winPoint(0);
        tennisScore.winPoint(0);
        tennisScore.winPoint(1);
        tennisScore.winPoint(1);
        tennisScore.winPoint(1);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Deuce");
    }
    @Test
    void deuce4_4() {
        tennisScore.winPoint(0);
        tennisScore.winPoint(0);
        tennisScore.winPoint(0);
        tennisScore.winPoint(0);
        tennisScore.winPoint(1);
        tennisScore.winPoint(1);
        tennisScore.winPoint(1);
        tennisScore.winPoint(1);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Deuce");
    }
    // If at least three points have been scored by each side
    // and a player has one more point than his opponent,
    // the score of the game is “Advantage” for the player in the lead.
    @Test
    void advantagePlayer1_WhenScoreIs4_3() {
        setPoints(4, 3);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Advantage Player1");
    }
    @Test
    void advantagePlayer1_WhenScoreIs5_4() {
        // hai sa visam
//        tennisScore.winPoints(0, 5); // cui si cate puncte
//        tennisScore.winPoints(1, 4); // cui si cate puncte
        // biz: nu frate, n-are sens. ca niciodata nu castiga un player 3 puncte o data./
        // atunci cand produ are un API naspa, pot sa-mi usurez testele creandu-mi un mic 'test framework' :
        // aici o metoda care cheama api-ul unfliendly pentru mine
        setPoints(5, 4);

        String actual = tennisScore.getScore();

        assertThat(actual).isEqualTo("Advantage Player1");
    }

    private void setPoints(int player1Points, int player2Points) {
        for (int i = 0; i < player1Points; i++) {
            tennisScore.winPoint(0);
        }
        for (int i = 0; i < player2Points; i++) {
            tennisScore.winPoint(1);
        }
    }

}
