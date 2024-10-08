package victor.testing.tennis;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreParameterizedTest {
  TennisScore tennisScore = new TennisScore();

  static Stream<Arguments> data() {
    return Stream.of(
        Arguments.of(1, 1, "Fifteen-Fifteen"),
        Arguments.of(1, 0, "Fifteen-Love")
    );
  }

  @ParameterizedTest(name = "Given player 1 scored {0} point(s) and player 2 scored {1} point((s), the score should be {2}")
  @MethodSource("data")
  void theTest(int player1Points, int player2Points, String expectedScore) {
    addPointsToPlayer(1, player1Points);
    addPointsToPlayer(2, player2Points);

    String actual = tennisScore.score();

    assertThat(actual).isEqualTo(expectedScore);
  }

  private void addPointsToPlayer(int playerNumber, int points) {
    for (int i = 0; i < points; i++) {
      tennisScore.addPoint(playerNumber);
    }
  }
}
