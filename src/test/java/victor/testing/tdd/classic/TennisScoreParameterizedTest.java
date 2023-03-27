package victor.testing.tdd.classic;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized.Parameter;
import victor.testing.tools.HumanReadableTestNames;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(HumanReadableTestNames.class) // makes test names look nice
public class TennisScoreParameterizedTest {
  TennisGame tennisGame = new TennisGame();

  public static List<Input> data() {
    return List.of(
      new Input(0,0, "Love - Love"),
//      new Input(0,66, "Love - Love"),
//      new Input(0,3450, "Love - Love"),
//      new Input(0,3460, "Love - Love"),
//      new Input(0,347330, "Love - Love"),
      new Input(0,0, "Love - Love"),
      new Input(1,1, "Fifteen - Fifteen"),
      new Input(1,3, "Fifteen - Forty"),
      new Input(5,5, "Deuce")
    );
  }
 private record Input(int player1Points, int player2Points, String expectedScore) {}

  @ParameterizedTest(name = "{0}")
  @MethodSource("data")
  void zaTest(Input input) {
    setPoints(input.player1Points, input.player2Points);
    String s = tennisGame.score();
    assertThat(s).isEqualTo(input.expectedScore );
  }

  private void setPoints(int player1Points, int player2Points) {
    earnsPoints(TennisSide.ONE, player1Points);
    earnsPoints(TennisSide.TWO, player2Points);
  }

  private void earnsPoints(TennisSide side, int points) {
    IntStream.range(0, points)
            .forEach(i -> tennisGame.earnsPoint(side));
  }
}
