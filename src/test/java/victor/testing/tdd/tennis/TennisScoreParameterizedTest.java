package victor.testing.tdd.tennis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.*;

public class TennisScoreParameterizedTest {
  TennisScore tennisScore = new TennisScore(); // fiecare @Test are instanta separata de TenniScore cu state in el curat initial.

  public static List<Arguments> data() {
    return List.of(
        of(0,0, "Love:Love"),
        of(1,0, "Fifteen:Love"),
        of(3,0, "Forty:Love"),
        of(3,3, "Deuce")
        );
  }

  @ParameterizedTest(name = "When player1 scored {0} and player2 scored {1} then the score is ''{2}''")
  @MethodSource("data")
  void deuce(int player1Points, int player2Points, String expectedScore) {
    addPoints(Player.ONE, player1Points);
    addPoints(Player.TWO, player2Points);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo(expectedScore);
  }
  // le folosim cand ai doar DATE care se modifica intre testele tale.
  //    de ex cand dezbati cu BIZU cerintele intr-un Excel.
  // NU e sanatos sa ai 7 param
  // NU e sanatos sa ai 3 boolen param, mai ales daca le folosesti asa: if (bouleanu3) assertEquals("sip-asta", aia)


// mini testing framework sa ne faca testele mai citibile
  private void addPoints(Player one, int points) {
    for (int i = 0; i < points; i++) {
      tennisScore.addPoint(one);
    }
  }


}




// o sa fie enum ? Player class ?