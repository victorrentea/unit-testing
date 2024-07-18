package victor.testing.tdd;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
  //The running score of each game is described in a
  // manner peculiar to tennis: scores from zero to
  // three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
  TennisScore tennisScore = new TennisScore();

  @Test
  void loveLove() {
    String actualScore = tennisScore.getScore();

    assertThat(actualScore).isEqualTo("Love:Love");
  }

  @Test
  void fifteenLove() {
    tennisScore.addPoint(1);

    String actualScore = tennisScore.getScore();

    assertThat(actualScore).isEqualTo("Fifteen:Love");
  }
  @Test
  void loveFifteen() {
    tennisScore.addPoint(2);

    String actualScore = tennisScore.getScore();

    assertThat(actualScore).isEqualTo("Love:Fifteen");
  }
  @Test
  void thirtyLove() {
    tennisScore.addPoint(1);
    tennisScore.addPoint(1);

    String actualScore = tennisScore.getScore();

    assertThat(actualScore).isEqualTo("Thirty:Love");
  }
}
