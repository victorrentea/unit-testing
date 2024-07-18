package victor.testing.tdd;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
//      @DisplayName()
//    @Description("Test List Starts With")

  void thirtyLove() {
    tennisScore.addPoint(1);
    tennisScore.addPoint(1);

    String actualScore = tennisScore.getScore();

    assertThat(actualScore).isEqualTo("Thirty:Love");
  }

//  @Description("allure") // here to comfort the business:
  // examples from requirements
  @Test // overlaping but left because biz said so. documentation
  void thirtyThirty() {
    tennisScore.addPoint(1);
    tennisScore.addPoint(1);
    tennisScore.addPoint(2);
    tennisScore.addPoint(2);

    String actualScore = tennisScore.getScore();

    assertThat(actualScore).isEqualTo("Thirty:Thirty");
  }

  @Test
  void fortyLove() {
    tennisScore.addPoint(1);
    tennisScore.addPoint(1);
    tennisScore.addPoint(1);

    String actualScore = tennisScore.getScore();

    assertThat(actualScore).isEqualTo("Forty:Love");
  }
}
