package victor.testing.tdd.classic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreDinToporCuForTest {

    private final TennisScore tennisScore = new TennisScore();


    private static final List<Object[]> combinatii = List.of(
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,0, "Love - Love"},
            new Object[]{0,3, "Love - Forty"}
            );

    @Test
    void unu() {
        // ASA NU
        for (Object[] objects : combinatii) {
            setPoints((Integer) objects[0], (Integer) objects[1]);
            //  TODO dupa: metode separate
            String actual = tennisScore.getScore();

            assertThat(actual).isEqualTo("Fifteen - Love");

        }
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
