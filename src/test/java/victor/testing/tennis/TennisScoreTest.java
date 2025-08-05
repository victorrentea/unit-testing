package victor.testing.tennis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {


  @ParameterizedTest
  void oneTestToRuleThemAll(int player1Score, int player2Score, String expectedString) {
    TennisScore tennisScore = new TennisScore();
    String actual = tennisScore.getScore();

    assertThat(actual).isEqualTo("Love-Love");
  }
  @Test
  void initialScore() {
    TennisScore tennisScore = new TennisScore();
    String actual = tennisScore.getScore();

    assertThat(actual).isEqualTo("Love-Love");
  }
  @Test
  void player1Scores() {
    TennisScore tennisScore = new TennisScore();
    tennisScore.addPoint(0);
    String actual = tennisScore.getScore();

    assertThat(actual).isEqualTo("Fifteen-Love");
  }
  @Test
  void player1ScoresTwice() {
    TennisScore tennisScore = new TennisScore();
    tennisScore.addPoint(0);
    tennisScore.addPoint(0);
    String actual = tennisScore.getScore();

    assertThat(actual).isEqualTo("Thirty-Love");
  }
  @Test
  void player1Scores3Points() {
    TennisScore tennisScore = new TennisScore();
    tennisScore.addPoint(0);
    tennisScore.addPoint(0);
    tennisScore.addPoint(0);
    String actual = tennisScore.getScore();

    assertThat(actual).isEqualTo("Forty-Love");
  }
  @Test
  void player2Scores() {
    TennisScore tennisScore = new TennisScore();
    tennisScore.addPoint(1); // TODO make an enum
    String actual = tennisScore.getScore();

    assertThat(actual).isEqualTo("Love-Fifteen");
  }
  @Test
  void exampleFromBizStory() {
    TennisScore tennisScore = new TennisScore();
    tennisScore.addPoint(0);
    tennisScore.addPoint(0);
    tennisScore.addPoint(0);
    tennisScore.addPoint(1);
    tennisScore.addPoint(1);
    String actual = tennisScore.getScore();

    assertThat(actual).isEqualTo("Forty-Thirty");
  }
}
//The running score of each game is described in a manner
// peculiar to tennis: scores from zero to three points
// are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
