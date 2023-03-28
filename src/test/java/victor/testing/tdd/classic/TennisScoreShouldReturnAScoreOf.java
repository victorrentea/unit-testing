package victor.testing.tdd.classic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.tools.HumanReadableTestNames;

import java.util.stream.IntStream;

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
//  @DisplayName("Human readable test name") //
  // i hate display name for the same reason I hate // comments
  void loveLoveForNewGame() {
    setPoints(0, 0);
    String s = tennisGame.score();
    assertThat(s).isEqualTo("Love - Love");
  }

  @Test
  void loveFifteenWhenPlayer2Scores() {
    setPoints(0, 1);
    String s = tennisGame.score();
    assertThat(s).isEqualTo("Love - Fifteen");
  }

  @Test
  void fifteenLoveWhenPlayer1Scores() {
    setPoints(1, 0);
    String s = tennisGame.score();
    assertThat(s).isEqualTo("Fifteen - Love");
  }

  // when the name of the test is not telling more than the body itself
  // and when you are testing combinations of data => think of using
  @Test
  void loveThirtyWhenPlayer2Scores2Points() {
    setPoints(0, 2);

    String s = tennisGame.score();
    assertThat(s).isEqualTo("Love - Thirty");
  }

  @Test
  void loveFortyWhenPlayer2Scores3Points() {
    setPoints(0, 3);

    String s = tennisGame.score();
    assertThat(s).isEqualTo("Love - Forty");
  }


  //3) If at least three points have been scored by each player,
  // and the scores are equal, // the score is “Deuce”.
  @Test
  void deuceWhenBothPlayersScore3Points() {
    setPoints(3, 3);

    String s = tennisGame.score();
    assertThat(s).isEqualTo("Deuce");
  }
  @Test
  void deuceWhenBothPlayersScore4Points() {
    setPoints(4, 4);

    String s = tennisGame.score();
    assertThat(s).isEqualTo("Deuce");
  }

  private void setPoints(int player1Points, int player2Points) {
    earnsPoints(TennisSide.ONE, player1Points);
    earnsPoints(TennisSide.TWO, player2Points);
  }

  private void earnsPoints(TennisSide side, int points) {
    IntStream.range(0, points)
            .forEach(i -> tennisGame.earnsPoint(side));
  }

}
