package victor.testing.tdd.classic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameParameterizedTest {
  TennisGame game = new TennisGame();

  public static Stream<Arguments> date() {
    return Stream.of(
        Arguments.of(7, 7, "Deuce"),
        Arguments.of(3, 2, "Forty:Thirty")
        // min 4-5 combinatii
    );
  }

  @ParameterizedTest(name = "If player one earns {0} points and player two earns {1} points then the score is {2}")
  // MAI NASPA DECAT FEATUREURI: pt acelasi motiv
  //  pt care code review e mai naspa ca pair programming
  @MethodSource("date")
  void zaTest(int player1Points, int player2Points, String expectedScoreString) {
    addPoints(player1Points, player2Points);
    String actual = game.getScore();
    assertThat(actual).isEqualTo(expectedScoreString);
  }

  // Testing DSL = (Domain Specific Language)
  // mini-framework: test-helpere
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
