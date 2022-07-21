package victor.testing.tdd.classic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameParameterized5Test {

   // The running score of each game is described in a manner peculiar to tennis:
   // scores from zero to three points are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
   private TennisScore tennisGame = new TennisScore();


   //@BeforeClass @BeforeAll // instead, create a custom @Rule
   // infant testing framework
   private void addPointsToPlayer(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.addPoint(player);
      }
   }

   // testing framework un pic mai elaborata
    private String translateScore(int pointsPlayer1, int pointsPlayer2) {
        addPointsToPlayer(Player.ONE, pointsPlayer1);
        addPointsToPlayer(Player.TWO, pointsPlayer2);

        return tennisGame.getScore();
   }

   public static Stream<Arguments> data() {
      return Stream.of(
         Arguments.of(1,1, "Fifteen - Fifteen"),
         Arguments.of(11,12, "Advantage Player2"),
         Arguments.of(1,0, "Fifteen - Love")
      );
   }

   @DisplayName("Given a new tenis game")
   @ParameterizedTest(name = "when player 1 scored {0} point(s) and player 2 scored {1} point(s), then the score should be {2}")
   @MethodSource("data")
   void givenANewTennisGame(int player1Score, int player2Score, String expectedValue) {
      // given
      String actual = translateScore(player1Score, player2Score);

      // then
      assertThat(actual).isEqualTo(expectedValue);
   }

}
