package victor.testing.tdd;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import victor.testing.tdd.TennisGame.Player;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

public class TennisGameParameterizedTest {
   public TennisGameParameterizedTest() {
//      System.out.println("O instanta noua per @Test!");
   }

   private String translateScore(int points1, int points2) {
      TennisGame tennisGame = new TennisGame();
      markPoints(Player.ONE, points1, tennisGame);
      markPoints(Player.TWO, points2, tennisGame);
      return tennisGame.score();
   }

   private void markPoints(Player player, int points, TennisGame tennisGame) {
      for (int i = 0; i < points; i++) {
         tennisGame.markPoint(player);
      }
   }

   private static Stream<Arguments> toateDatele() {
      return Stream.of(
          of(0, 0, "Love - Love"),
          of(0, 1, "Love - Fifteen"),
          of(1, 0, "Fifteen - Love"),
          of(1, 1, "Fifteen - Fifteen"),
          of(6, 6, "Deuce")
      );
   }

   @ParameterizedTest
   @MethodSource("toateDatele")
   public void loveLove(int points1, int points2, String expectedScore) {
      assertThat(translateScore(points1, points2)).isEqualTo(expectedScore);
   }


}
