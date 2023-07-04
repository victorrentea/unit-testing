package victor.testing.tdd.tennis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {
  @Test
  void loveLove() {
    String score = TennisScore.getScore();
    assertThat(score).isEqualTo("Love:Love");
  }
  @Test
  void fifteenLove() {
    TennisScore.addPoint(Player.ONE);
    String score = TennisScore.getScore();
    assertThat(score).isEqualTo("Fifteen:Love");
  }
  @Test
  void loveFifteen() {
    TennisScore.addPoint(Player.TWO);
    String score = TennisScore.getScore();
    assertThat(score).isEqualTo("Love:Fifteen");
  }
  @Test
  void fifteenFifteen() {
    TennisScore.addPoint(Player.ONE);
    TennisScore.addPoint(Player.TWO);
    String score = TennisScore.getScore();
    assertThat(score).isEqualTo("Fifteen:Fifteen");
  }

  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
}


// o sa fie enum ? Player class ?