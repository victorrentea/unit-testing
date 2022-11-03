package victor.testing.tdd.classic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

public class TennisGameParameterized5Test {

   // The running score of each game is described in a manner peculiar to tennis:
   // scores from zero to three points are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
   private TennisScore tennisGame = new TennisScore();


   //@BeforeClass @BeforeAll // instead, create a custom @Rule
   // infant testing framework
   private void addPointsToPlayer(int playerNumber, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.addPoint(playerNumber==1?Player.ONE:Player.TWO);
      }
   }

   // custom Domain Specific Language (DSL)
   private void setPoints(int points1, int points2) {
      addPointsToPlayer(1, points1);
      addPointsToPlayer(2, points2);
   }

   private String translateScore(int pointsPlayer1, int pointsPlayer2) {
      setPoints(pointsPlayer1, pointsPlayer2);

      return tennisGame.getScore();
   }

   public static Stream<TheOneTestData> data() {
      return Stream.of(
         new TheOneTestData(1,1, "Fifteen:Fifteen"),
         new TheOneTestData(12,12, "Deuce"),
         new TheOneTestData(1,0, "Fifteen:Love")
      );
   }

   record TheOneTestData(int player1Points, int player2Points, String expectedValue) {}

   @ParameterizedTest(name = "Given player 1 scored {0} point(s) and player 2 scored {1} point((s), the score should be {2}")
   @MethodSource("data")
   void theOneTest(TheOneTestData data) {
      // given
      String actual = translateScore(data.player1Points, data.player2Points);

      // then
      assertThat(actual).isEqualTo(data.expectedValue);
   }

}
