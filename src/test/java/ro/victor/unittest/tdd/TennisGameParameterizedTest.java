package ro.victor.unittest.tdd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class TennisGameParameterizedTest {
   private final int player1Score;
   private final int player2Score;
   private final String expectedScoreString;

   public TennisGameParameterizedTest(int player1Score, int player2Score, String expectedScoreString) {
      this.player1Score = player1Score;
      this.player2Score = player2Score;
      this.expectedScoreString = expectedScoreString;
   }

   private String translate(int player1Score, int player2Score) { // e pura ?
      TennisGame tennisGame = new TennisGame();
      addPointsToPlayer(tennisGame, Players.PLAYER1, player1Score);
      addPointsToPlayer(tennisGame, Players.PLAYER2, player2Score);
      return tennisGame.score();
   }

   private void addPointsToPlayer(TennisGame tennisGame, Players player, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.scoresPoint(player);
      }
   }

   @Parameters(name = "Score for ({0}, {1}) = {2}")
   public static List<Object[]> parameters() {
//      File folder = new File("/src/test/resources/xslt-data");
//      Arrays.stream(folder.listFiles())
//          .filter(file -> file.getName().endsWith(".in.xml"))
//          .collect(Collectors.toList());

      return Arrays.asList(
          new Object[] {0, 0, "Love - Love"},
          new Object[] {5, 4, "Advantage Player1"},
          new Object[] {0, 1, "Love - Fifteen"} //samd
      );

      //
   }

   @Test
   public void newGame() {
      assertThat(translate(player1Score, player2Score)).isEqualTo(expectedScoreString);
   }



}
