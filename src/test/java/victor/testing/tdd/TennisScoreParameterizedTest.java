package victor.testing.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreParameterizedTest {
  //The running score of each game is described in a
  // manner peculiar to tennis: scores from zero to
  // three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
  TennisScore tennisScore = new TennisScore();

  public static Stream<Arguments> testCases() {
    return Stream.of(
        Arguments.of(0, 0, "Love:Love"),
        Arguments.of(1, 0, "Fifteen:Love"),
        Arguments.of(0, 1, "Love:Fifteen"),
        Arguments.of(2, 0, "Thirty:Love"),
        Arguments.of(1, 1, "Fifteen:Fifteen"),
        Arguments.of(3, 0, "Forty:Love"),
        Arguments.of(2, 1, "Thirty:Fifteen"),
        Arguments.of(1, 2, "Fifteen:Thirty"),
        Arguments.of(0, 3, "Love:Forty"),
        Arguments.of(2, 2, "Thirty:Thirty"),
        Arguments.of(3, 3, "Deuce"),
        Arguments.of(4, 4, "Deuce"),
        Arguments.of(5, 5, "Deuce"),
        Arguments.of(6, 6, "Deuce"),
        Arguments.of(7, 7, "Deuce"),
        Arguments.of(8, 8, "Deuce"),
        Arguments.of(9, 9, "Deuce"),
        Arguments.of(10, 10, "Deuce"),
        Arguments.of(4, 3, "Advantage Player1"),
        Arguments.of(5, 4, "Advantage Player1"),
        Arguments.of(6, 5, "Advantage Player1"),
        Arguments.of(7, 6, "Advantage Player1"),
        Arguments.of(3, 4, "Advantage Player2"),
        Arguments.of(4, 5, "Advantage Player2"),
        Arguments.of(5, 6, "Advantage Player2"),
        Arguments.of(6, 7, "Advantage Player2"),
        Arguments.of(4, 0, "Game Player1"),
        Arguments.of(0, 4, "Game Player2"),
        Arguments.of(4, 1, "Game Player1"),
        Arguments.of(1, 4, "Game Player2"),
        Arguments.of(4, 2, "Game Player1"),
        Arguments.of(2, 4, "Game Player2"),
        Arguments.of(4, 3, "Game Player1"),
        Arguments.of(3, 4, "Game Player2")
    );
  }
  @ParameterizedTest(name="Player1: {0}, Player2: {1} => {2}")
  @MethodSource("testCases")
  void theOnlyOneTest(int player1Points, int player2Points, String expectedScore) {
    for (int i = 0; i < player1Points; i++) {
      tennisScore.addPoint(1);
    }
    for (int i = 0; i < player2Points; i++) {
      tennisScore.addPoint(2);
    }

    String actualScore = tennisScore.getScore();

    assertThat(actualScore).isEqualTo(expectedScore);
  }
}
