package victor.testing.tdd;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreParameterizedTest {


   private TennisScore tennisScore = new TennisScore();

   private void setPoints(int points1, int points2) {
      setPointsForPlayer(Player.ONE, points1);
      setPointsForPlayer(Player.TWO, points2);
   }
   private void setPointsForPlayer(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisScore.winsPoint(player);
      }
   }


   public static List<Object[]> dateDeTest() {
      return Arrays.asList(
          new Object[]{0, 0, "Love-Love"},
          new Object[]{1, 0, "Fifteen-Love"},
          new Object[]{3, 0, "Forty-Love"},
          new Object[]{3, 3, "Deuce"},
          new Object[]{3, 4, "Advantage Player2"}
      );
   }

   @ParameterizedTest(name="Scorul pt {0} {1} este {2}")
   @MethodSource("dateDeTest")
   public void zaTest(int point1, int point2, String expected) {
      setPoints(point1, point2);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo(expected);

   }
}
