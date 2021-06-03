package victor.testing.tdd;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TennisScoreTest {
   @Test
   public void initialScore() {
      String actual = TennisScore.getScore();

      assertThat(actual).isEqualTo("Love - Love");
   }
   // The running score of each game is described in a manner
   // peculiar to tennis: scores from zero to three points
   // are described as “Love”, “Fifteen”, “Thirty”, and “Forty”
   // respectively.
   @Test
   public void player1Scores1Point() {
      TennisScore.scorePoint(Players.ONE);
      String actual = TennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen - Love");
   }
}
