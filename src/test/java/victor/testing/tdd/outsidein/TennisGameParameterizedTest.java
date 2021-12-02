package victor.testing.tdd.outsidein;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import victor.testing.tdd.outsidein.TennisGame.Player;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TennisGameParameterizedTest {

   // The running score of each game is described in a manner peculiar to tennis:
   // scores from zero to three points are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
   private TennisGame tennisGame = new TennisGame();


   //@BeforeClass @BeforeAll // instead, create a custom @Rule
   // infant testing framework
   private void addPointsToPlayer(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.playerWonPoint(player);
      }
   }

   // custom Domain Specific Language (DSL)
   private void setPoints(int points1, int points2) {
      addPointsToPlayer(Player.ONE, points1);
      addPointsToPlayer(Player.TWO, points2);
   }

   private String translateScore(int pointsPlayer1, int pointsPlayer2) {
      setPoints(pointsPlayer1, pointsPlayer2);

      return tennisGame.getScore();
   }

   public static Stream<Arguments> data() {
      return Stream.of(
         Arguments.of(1,1, "Fifteen:Fifteen"),
         Arguments.of(1,0, "Fifteen:Love")
      );

   }

   @ParameterizedTest(name = "Given player 1 scored {0} point(s) and player 2 scored {1} point((s), the score should be {2}")
   @MethodSource("data")
   void theTest(int player1Score, int player2Score, String expectedValue) {
      // given
      String actual = translateScore(player1Score, player2Score);

      // then
      assertThat(actual).isEqualTo(expectedValue);
   }

   @Test
   void throwsForNullPlayer() {  // must have : for utility/library code
      assertThatThrownBy(() -> tennisGame.playerWonPoint(null))
          .hasMessage("Player must not be null")
          .isInstanceOf(NullPointerException.class);
   }

}
