package ro.victor.unittest.tdd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class TennisScoreParameterizedTests {
   private TennisScore tennisScore = new TennisScore();
   private final int score1;
   private final int score2;
   private final String expectedScore;

   public TennisScoreParameterizedTests(int score1, int score2, String expectedScore) {
      this.score1 = score1;
      this.score2 = score2;
      this.expectedScore = expectedScore;
   }
   @Parameters(name = "Score for {0},{1} is {2}")
   public static List<Object[]> arguments() {
      return Arrays.asList(
          new Object[]{0,0,"Love-Love"},
          new Object[]{0,1,"Love-Fifteen"},
          new Object[]{5,5,"Deuce"}
          );

      // Verificam o transformata de fisier (XSLT XML--->XML sau JSON--->XML)
      // am facut test1.in.json, test1.out.xml, test2..... test3...
      // Files.in folder---> construiam cazurile de test pe baza unor fisiere din src/test/resources
   }

   private String translateScore(int score1, int score2) {
      setScoreForPlayer(score1, Player.ONE);
      setScoreForPlayer(score2, Player.TWO);
      return tennisScore.score();
   }
   private void setScoreForPlayer(int points, Player player) {
      for (int i = 0; i < points; i++) {
         tennisScore.pointWon(player);
      }
   }



   @Test
   public void returnsLoveLoveForNewGame() {
      assertThat(translateScore(score1, score2)).isEqualTo(expectedScore);
   }

}
