package victor.testing.tdd.classic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

public class TennisGameParameterizedTest {

   private TennisGame tennisGame = new TennisGame();

   public static Stream<Arguments> testCombinations() {
      return Stream.of(
          of(0, 0, "Love - Love"),
          of(1, 0, "Fifteen - Love")
      );

   }

   @ParameterizedTest
   @MethodSource("testCombinations")
   void returnsLoveFifteen_whenSecondPlayerScores1Point(int player1Points, int player2Points, String expectedScore) {
      addPointsToPlayer(Player.ONE, player1Points);
      addPointsToPlayer(Player.TWO, player2Points);

      String actual = tennisGame.getScore();

      assertThat(actual).isEqualTo(expectedScore);
   }

   // building a 'testing framework' - is it good or bad.
   // - risk of bugs
   // + simpler shorter tests
   private void addPointsToPlayer(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.addPoint(player);
      }
   }

}
