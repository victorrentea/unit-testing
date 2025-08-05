package victor.testing.tennis;

import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TennisScorePTest {

  public static Stream<TennisTestCase> data() {
    return Stream.of(
        new TennisTestCase(0, 0, "Love-Love"),
        new TennisTestCase(1, 0, "Fifteen-Love"),
        new TennisTestCase(2, 0, "Thirty-Love"),
        new TennisTestCase(3, 0, "Forty-Love"),
        new TennisTestCase(0, 1, "Love-Fifteen"),
        new TennisTestCase(0, 2, "Love-Thirty"),
        new TennisTestCase(0, 3, "Love-Forty"),
        new TennisTestCase(1, 1, "Fifteen-Fifteen"),
        new TennisTestCase(2, 2, "Thirty-Thirty"),
        new TennisTestCase(3, 3, "Deuce"),
        new TennisTestCase(4, 4, "Deuce")
    );
  }
  record TennisTestCase(
      int player1Score,
      int player2Score,
      String expectedString
//      boolean shouldHaveSentKNotif,
//      boolean shouldHaveSentKNotif2,
//      boolean shouldHaveInsert,
//      boolean shouldHavethrownAnExc,
  ){
    @Override
    public @NonNull String toString() {
      return player1Score+":"+player2Score+"=>"+expectedString;
    }
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("data")
  void oneTestToRuleThemAll(TennisTestCase testCase) {
    // given (preparing the fixture and test data: new, when..thenReturn, insert
    TennisScore tennisScore = new TennisScore();
    for (int i = 0; i < testCase.player1Score; i++) {
      tennisScore.addPoint(0);
    }
    for (int i = 0; i < testCase.player2Score; i++) {
      tennisScore.addPoint(1);
    }

    // when = call to the tested code
    String actual = tennisScore.getScore();

    // then: effects: assert/verify/repo.find/klistener.receive
    assertThat(actual).isEqualTo(testCase.expectedString());
  }
}
//The running score of each game is described in a manner
// peculiar to tennis: scores from zero to three points
// are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
