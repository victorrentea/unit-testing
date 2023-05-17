package victor.testing.tdd.classic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class TennisGameParameterizedJava17Test {
  TennisGame game = new TennisGame();

  public static Stream<Input> date() {
    return Stream.of(
        new Input(7, 7, "Deuce"),
        new Input(3, 2, "Forty:Thirty")
        // min 4-5 combinatii
    );
  }
  private record Input(int points1, int points2, String expectedScoreString){}

  @ParameterizedTest
  @MethodSource("date")
  void zaTest(Input input) {
    addPoints(input.points1, input.points2);
    String actual = game.getScore();
    assertThat(actual).isEqualTo(input.expectedScoreString);
  }
  public void addPoints(int player1Points, int player2Points) {
    addPointsToPlayer(Player.ONE, player1Points);
    addPointsToPlayer(Player.TWO, player2Points);
  }
  public void addPointsToPlayer(Player player, int points) {
    for (int i = 0; i < points; i++) {
      game.addPoint(player);
    }

  }
}
