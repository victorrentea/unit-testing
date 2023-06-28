package victor.testing.tdd.tennis;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.*;

public class TennisScoreParameterizedTest {
  TennisScore tennisScore = new TennisScore();

  public static Stream<Arguments> testData() {
    return Stream.of(
        of(1, 1, "Fifteen-Fifteen"),
        of(1, 2, "Fifteen-Thirty"),
        of(1, 0, "Fifteen-Love"),
        of(0, 1, "Love-Fifteen"),
        of(3, 0, "Forty-Love")
    );
  }

  @ParameterizedTest(name = "When player 1 scored {0} points and player 2 scored {1} points, the score is ''{2}''")
  @MethodSource("testData")
  void oneTestToRuleThemAll(int player1Points, int player2Points, boolean expectedScore) {
    for (int i = 0; i < player1Points; i++) {
      tennisScore.player1Scored();
    }
    for (int i = 0; i < player2Points; i++) {
      tennisScore.player2Scored();
    }
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo(expectedScore);
  }

}
