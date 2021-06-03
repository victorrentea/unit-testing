package victor.testing.tdd;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TennisScoreTest {

   private TennisScore tennisScore = new TennisScore();

   @Test
   public void initialScore() {
      String actual = tennisScore.getScore();
      assertThat(actual).isEqualTo("Love - Love");
   }
   // “Love”, “Fifteen”, “Thirty”, and “Forty”
   @Test
   public void player1Scores1Point() {
      tennisScore.scorePoint(Players.ONE);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen - Love");
   }
   @Test
   public void player2Scores1Point() {
      tennisScore.scorePoint(Players.TWO);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Love - Fifteen");
   }

//   @Test
//   public void bothPlayersScore1Point() {
//      tennisScore.scorePoint(Players.ONE);
//      tennisScore.scorePoint(Players.TWO);
//      String actual = tennisScore.getScore();
//
//      assertThat(actual).isEqualTo("Fifteen - Fifteen");
//   }
}
