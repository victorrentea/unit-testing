package victor.testing.junit;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import victor.testing.tennis.TennisScore;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;

public class Parameterized5Tests {
  private final TennisScore score = new TennisScore();

  public static Stream<TestCase> data() {
    return Stream.of(
        new TestCase(0, 0, "Love-Love"),
        new TestCase(1, 0, "Fifteen-Love"),
        new TestCase(2, 0, "Thirty-Love"),
        new TestCase(3, 0, "Forty-Love"),
        new TestCase(0, 1, "Love-Fifteen"),
        new TestCase(0, 2, "Love-Thirty"),
        new TestCase(0, 3, "Love-Forty"),
        new TestCase(1, 1, "Fifteen-Fifteen"),
        new TestCase(2, 2, "Thirty-Thirty"),
        new TestCase(3, 3, "Deuce"),
        new TestCase(4, 4, "Deuce"),
        new TestCase(5, 5, "Deuce"),
        new TestCase(4, 3, "Advantage Player1"),
        new TestCase(3, 4, "Advantage Player2"),
        new TestCase(4, 0, "Game won Player1"),
        new TestCase(0, 4, "Game won Player2"),
        new TestCase(4, 2, "Game won Player1"),
        new TestCase(2, 4, "Game won Player2"),
        new TestCase(4, 5, "Advantage Player2"),
        new TestCase(5, 4, "Advantage Player1")
    );
  }

  private record TestCase(int player1Points, int player2Points, String expectedScore) {
  }

  @ParameterizedTest
  @MethodSource("data")
  public void oneTest(TestCase testCase) {
    for (int i = 0; i < testCase.player1Points; i++) {
      score.addPoint(1);
    }
    for (int i = 0; i < testCase.player2Points; i++) {
      score.addPoint(2);
    }
    assertThat(score.score()).isEqualTo(testCase.expectedScore);

//    if (testCase.obscureFlag) {
//      verify(mock).method()
//    }
//    if (testCase.obscureFlag2) {
//      verify(mock).method()
//    }
//    if (testCase.obscureFlag3) {
//      verify(mock).method()
//    }
//    if (testCase.obscureFlag4) {
//      verify(mock).method()
//    }
  }

}
