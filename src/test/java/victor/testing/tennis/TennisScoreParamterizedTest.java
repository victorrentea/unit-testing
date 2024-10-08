package victor.testing.tennis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class TennisScoreParamterizedTest {
  TennisScore tennisScore = new TennisScore();

  public static Stream<TestCase> data() {
    return Stream.of(
        new TestCase(0, 0, "Love-Love"),
        new TestCase(0, 1, "Love-Fifteen"),
        new TestCase(1, 0, "Fifteen-Love"),
        new TestCase(2, 0, "Thirty-Love"),
        new TestCase(3, 0, "Forty-Love"),
        new TestCase(3, 3, "Deuce"),
        new TestCase(7, 6, "Advantage Player1"),
        new TestCase(3, 4, "Advantage Player2"),
        new TestCase(4, 0, "Game won Player1"),
        new TestCase(7, 5, "Game won Player1"),
        new TestCase(0, 4, "Game won Player2")
    );
  }
  record TestCase(int player1Score, int player2Score, String expectedScore) {
  }
  @ParameterizedTest
  @MethodSource("data")
  void theTest(TestCase testCase) {
    addPointsToPlayer(1, testCase.player1Score);
    addPointsToPlayer(2, testCase.player2Score);

//    if (expectedToThrow) {
//      Assertions.assertThatThrownBy(() -> tennisScore.score())
//          .isInstanceOf(IllegalArgumentException.class);
//    } else {
      String score = tennisScore.score();
      Assertions.assertThat(score).isEqualTo(testCase.expectedScore);
//    }
  }

  private void addPointsToPlayer(int playerNumber, int playerScore) {
    for (int i = 0; i < playerScore; i++) {
      tennisScore.addPoint(playerNumber);
    }
  }
}
