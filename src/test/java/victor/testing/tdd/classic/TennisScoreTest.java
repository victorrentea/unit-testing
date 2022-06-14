package victor.testing.tdd.classic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
    @BeforeEach
    final void before() {

    }
    @Test
    void newGame() {
        String actual = TennisScore.getScore();

        String expected = "Love - Love";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void fifteenLove() {
        TennisScore.winPoint(0); // cum altfel pot spune "jucatorul 1"?
        //  TODO dupa: metode separate
        String actual = TennisScore.getScore();

        String expected = "Fifteen - Love";
        assertThat(actual).isEqualTo(expected);
    }

}
