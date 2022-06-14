package victor.testing.tdd.classic;

import org.assertj.core.api.Assertions;
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
        String actual = new TennisScore().getScore();

        String expected = "Love - Love";
        assertThat(actual).isEqualTo(expected);
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

}
