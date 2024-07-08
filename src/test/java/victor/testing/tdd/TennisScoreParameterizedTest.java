package victor.testing.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TennisScoreParameterizedTest {

  private TennisScore tennisScore = new TennisScore();

  // test-helper method
  private void setPoints(int player1Points, int player2Points) {
    for (int i = 0; i < player1Points; i++) {
      tennisScore.addPointToPlayer1();
    }
    for (int i = 0; i < player2Points; i++) {
      tennisScore.addPointToPlayer2();
    }
  }

  public static List<TestCase> testData() {
    return List.of(
        new TestCase(0, 0, "Love - Love"),
        new TestCase(1, 0, "Fifteen - Love"),
        new TestCase(2, 0, "Thirty - Love"),
        new TestCase(3, 0, "Forty - Love"),
        new TestCase(0, 1, "Love - Fifteen"),
        new TestCase(0, 2, "Love - Thirty"),
        new TestCase(0, 3, "Love - Forty"),
        new TestCase(1, 1, "Fifteen - Fifteen"),
        new TestCase(2, 2, "Thirty - Thirty"),
//        new TestCase(3, 3, "Deuce"),
//        new TestCase(4, 3, "Advantage Player1"),
//        new TestCase(3, 4, "Advantage Player2"),
//        new TestCase(4, 4, "Deuce"),
//        new TestCase(5, 4, "Advantage Player1"),
//        new TestCase(4, 5, "Advantage Player2"),
        new TestCase(6, 4, "Game won Player1")
//        new TestCase(4, 6, "Game won Player2"),
    );
  }
  // #1) more than 3 params => do this:
  record TestCase(
      int player1Points,
      int player2Points,
      String expectedScore) {
    public String toString() {
      return player1Points + " - " + player2Points + " => " + expectedScore;
    }
  }

  // #2) Avoid params of type boolean or having 2 values. Prefer numbers. prefer params that enter in combination with many others.
  // also to avoid having if() in tests

  @ParameterizedTest(name = "{0}")
  @MethodSource("testData")
  void parameterizedTest(TestCase testCase) {
    setPoints(testCase.player1Points(), testCase.player2Points());

    String actual = tennisScore.getScore();

//    if (testCase.flag42()) assertEquals(); // avoid
    assertEquals(testCase.expectedScore(), actual);
  }
}
