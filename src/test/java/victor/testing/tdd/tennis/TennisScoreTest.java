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
  @Test
  void thirtyLove() {
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Thirty:Love");
  }
  @Test
  void fortyLove() {
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Forty:Love");
  }
  // Ati avut vreodata in spec/req "Exemple" de la biz ?
  // toate acele "exemple" merg direct in Teste automate.

  @Test
  void fortyThirty() { // e direct verde, dar reprezinta exemplu de la Biz citire.
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.TWO);
    tennisScore.addPoint(Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Forty:Thirty");
  }

  // If at least three points have been scored by each player,
  // and the scores are equal, the score is “Deuce”.
  @Test
  void deuce() {
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.TWO);
    tennisScore.addPoint(Player.TWO);
    tennisScore.addPoint(Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Deuce");
  }
  @Test
  void deuce6() {
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.ONE);
    tennisScore.addPoint(Player.TWO);
    tennisScore.addPoint(Player.TWO);
    tennisScore.addPoint(Player.TWO);
    tennisScore.addPoint(Player.TWO);
    tennisScore.addPoint(Player.TWO);
    tennisScore.addPoint(Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Deuce");
  }



}




// o sa fie enum ? Player class ?