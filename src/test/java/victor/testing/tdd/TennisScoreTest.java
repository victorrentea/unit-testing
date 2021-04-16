package victor.testing.tdd;
// String[]
//      let (score1, score2) = f(); ///"Love", "Love"
// Map<playerNo:int/enum, String>
// Player.getScore

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TennisScoreTest {

   private TennisScore tennisScore = new TennisScore();


   public TennisScoreTest() {
      System.out.println("one new test instance per @Test");
   }


   @Test
   public void initial() {
      String actual = tennisScore.getScore();
      assertThat(actual).isEqualTo("Love - Love");
   }

   @Test
   public void awayPlayerWinsAPoint() {
      tennisScore.addPoint(Player.AWAY);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Love - Fifteen");
   }
   @Test
   public void bothPlayersWinsAPoint() {
      tennisScore.addPoint(Player.HOME );
      tennisScore.addPoint(Player.AWAY);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen - Fifteen");
   }

   @Test
   public void homePlayerWinsAPoint() {
      tennisScore.addPoint(Player.HOME);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Fifteen - Love");
   }
   @Test
   public void homePlayerWins2Points() {
      tennisScore.addPoint(Player.HOME);
      tennisScore.addPoint(Player.HOME);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Thirty - Love");
   }
   @Test
   public void homePlayerWins3Points() {
      tennisScore.addPoint(Player.HOME);
      tennisScore.addPoint(Player.HOME);
      tennisScore.addPoint(Player.HOME);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Forty - Love");
   }
   @Test
   public void bothPlayerWins3Points() {
      setPoints(3,3);

      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Deuce");
   }
   @Test
   public void bothPlayerWins4Points() {
      setPoints(4, 4);
      String actual = tennisScore.getScore();

      assertThat(actual).isEqualTo("Deuce");
   }

   //test helper functions (mini-testing framework)

   public void setPoints(int homePoints, int awayPoints) {
      addPoints(Player.HOME, homePoints);
      addPoints(Player.AWAY, awayPoints);
   }
   public void addPoints(Player player, int points) {
      for (int i = 0; i < points; i++) {
         tennisScore.addPoint(player);
      }
   }

   // If at least three points have been scored by each player, and the scores are equal,
   // the score is “Deuce”.

}
