package victor.testing.tdd.classic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import victor.testing.tdd.classic.TennisScore.Player;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameParameterized5Test {

   // The running score of each game is described in a manner peculiar to tennis:
   // scores from zero to three points are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
   private TennisScore tennisScore = new TennisScore();

   private void addPointsToPlayer(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisScore.winsPointByPlayer(player);
      }
   }

   // custom Domain Specific Language (DSL)
   private void setPoints(int points1, int points2) {
      addPointsToPlayer(Player.ONE, points1);
      addPointsToPlayer(Player.TWO, points2);
   }

   private String translateScore(int pointsPlayer1, int pointsPlayer2) {
      setPoints(pointsPlayer1, pointsPlayer2);

      return tennisScore.currentScore();
   }

   public static Stream<Arguments> data() {
      return Stream.of(
         Arguments.of(1,1, "Fifteen-Fifteen"),
         Arguments.of(1,0, "Fifteen-Love")
      );
   }

   @ParameterizedTest(name = "Given player 1 scored {0} point(s) and player 2 scored {1} point(s), the score should be {2}")
   @MethodSource("data")
   void theTest(int player1Score, int player2Score, String expectedValue) {
      // when
      String actual = translateScore(player1Score, player2Score);

      // then
      assertThat(actual).isEqualTo(expectedValue);
   }

}
