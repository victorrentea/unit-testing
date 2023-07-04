package victor.testing.tdd.tennis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
  TennisScore tennisScore = new TennisScore(); // fiecare @Test are instanta separata de TenniScore cu state in el curat initial.
  @Test
  void loveLove() {
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Love:Love");
  }
  @Test
  void fifteenLove() {
    tennisScore.addPoint(Player.ONE);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Fifteen:Love");
  }
  @Test
  void loveFifteen() {
    tennisScore.addPoint(Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Love:Fifteen");
  }
  @Test
  void fifteenFifteen() {
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Fifteen:Fifteen");
  }

  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
}


// o sa fie enum ? Player class ?