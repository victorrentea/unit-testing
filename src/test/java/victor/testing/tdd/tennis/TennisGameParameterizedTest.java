package victor.testing.tdd.tennis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameParameterizedTest {
  private TennisGame tennisGame = new TennisGame();

  record MyTestCase(int a, int b, String expected) {}
//  public static Stream<MyTestCase> data() {
  public static Stream<Arguments> data() {
    return Stream.of(
        Arguments.of(1, 2, "Fifteen-Thirty"/*, false, true, false*/),
        Arguments.of(7, 7, "Deuce")
    );
  }

  @ParameterizedTest(name = "If the player 1 scores {0} and player {1} then the score is ''{2}''")
  @MethodSource("data")
  void one(int player1Points, int player2Points, String expected/*, boolean shouldSendKafka*/) {
    addPoints(Player.ONE, player1Points);
    addPoints(Player.TWO, player2Points);

    String actual = tennisGame.getScore();

    assertThat(actual).isEqualTo(expected);
//    if (shouldSendKafka) {
//      verify(kafkaSender).send()
//    }
  }

  What's better than parameterized tests? what's better than writing tests alone?
    ==> .feature files

  private void addPoints(Player onplayer, int points) {
    for (int i = 0; i < points; i++) {
      tennisGame.addPoint(onplayer);
    }
  }
}
