package victor.testing.tdd.tennis;

import lombok.Value;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameParameterizedTest {
  TennisGame tennisGame = new TennisGame();

  public static Stream<TestCase> testData() {
    return Stream.of(
        new TestCase(0, 0, "Love-Love"),
        new TestCase(3, 2, "Forty-Thirty"),
        new TestCase(10, 10, "Deuce")
    );
  }

  @Value
  private static class TestCase {
    int player1Points;
    int player2Points;
    String expectedScore;
    @Override
    public String toString() {
      return "TestCase{" +
             "player1Points=" + player1Points +
             ", player2Points=" + player2Points +
             ", expectedScore='" + expectedScore + '\'' +
             '}';
    }
  }


  @ParameterizedTest(name = "{0}")
  @MethodSource("testData")
  void oneTestToRuleThemAll(TestCase testCase) {
    addPointsToPlayer(testCase.getPlayer1Points(), 1);
    addPointsToPlayer(testCase.getPlayer2Points(), 2);
    String score = tennisGame.getHumanReadableScore();
    assertThat(score).isEqualTo(testCase.getExpectedScore());
  }

  private void addPointsToPlayer(int points, int player) {
    for (int i = 0; i < points; i++) {
      if (player == 1) {
        tennisGame.addPointToPlayer1();
      } else {
        tennisGame.addPointToPlayer2();
      }
    }
  }
}
