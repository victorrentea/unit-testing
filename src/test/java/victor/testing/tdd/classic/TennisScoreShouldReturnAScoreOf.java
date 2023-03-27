package victor.testing.tdd.classic;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.tools.HumanReadableTestNames;

import static org.assertj.core.api.Assertions.assertThat;

  //1) A game is won by the first player to have won
  // at least four points in total and at least two points more than the opponent.
  //3) If at least three points have been scored by each player, and the scores are equal, the score is “Deuce”.
  //4) If at least three points have been scored by each side and a player has one more point than his opponent, the score of the game is “Advantage” for the player in the lead.

@DisplayNameGeneration(HumanReadableTestNames.class) // makes test names look nice


  // TDD rules:
  // - no change in prod unless to fix a test
  // - fix a test the fastest (most obvious) way possible
public class TennisScoreShouldReturnAScoreOf {
  TennisGame tennisGame = new TennisGame();

  public TennisScoreShouldReturnAScoreOf() {
    System.out.println("Would JUnit create new instances for each @Test");
  }

  @Test
  void loveLoveForNewGame() {
    String s = tennisGame.score();
    assertThat(s).isEqualTo("Love - Love");
  }

  @Test
  void loveFifteenWhenPlayer2Scores() {
    tennisGame.earnsPoint(TennisSide.TWO);
    String s = tennisGame.score();
    assertThat(s).isEqualTo("Love - Fifteen");
  }

  @Test
  void fifteenLoveWhenPlayer1Scores() {
    tennisGame.earnsPoint(TennisSide.ONE);
    String s = tennisGame.score();
    assertThat(s).isEqualTo("Fifteen - Love");
  }

  // when the name of the test is not telling more than the body itself
  // and when you are testing combinations of data => think of using
  @Test
  void loveThirtyWhenPlayer2Scores2Points() {
    tennisGame.earnsPoint(TennisSide.TWO);
    tennisGame.earnsPoint(TennisSide.TWO);
    String s = tennisGame.score();
    assertThat(s).isEqualTo("Love - Thirty");
  }
//  void scoreIsDeuceWhenPlayer1Scores3PointAndPlayer2Score3Points() {

  //2) The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points are described as
  // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
}
