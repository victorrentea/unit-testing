package victor.testing.tennis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class TennisScoreTest {

  static Stream<Arguments> scoreCases() {
    return Stream.of(
        of(0, 0, "Love-Love"),
        of(1, 0, "Fifteen-Love"),
        of(0, 1, "Love-Fifteen"),
        of(1, 1, "Fifteen-Fifteen"),
        of(0, 2, "Love-Thirty")
//        of(4, 0, "Game Won Player 1"),
//        of(5, 3, "Game Won Player 1"),
//        of(6, 4, "Game Won Player 1")
    );
  }

  @ParameterizedTest
  @MethodSource("scoreCases")
  void score(int player1Score, int player2Score, String expectedScore) {
    TennisScore score = new TennisScore(player1Score, player2Score);
    assertThat(score.getScore()).isEqualTo(expectedScore);
  }


}
