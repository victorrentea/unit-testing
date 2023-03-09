package victor.testing.tdd.classic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import victor.testing.tools.ReplaceCamelCase;

import static org.assertj.core.api.Assertions.assertThat;

// The game consists of 10 frames. In each frame the player has two rolls to knock down 10 pins.
// The score for the frame is the total number of pins knocked down, plus bonuses for 'strikes' and 'spares'.
// The score should be calculated at every point in game.

// A 'spare' is when the player knocks down all 10 pins in two rolls.
// The bonus for that frame is the number of pins knocked down by the next 1 roll.
//
// A 'strike' is when the player knocks down all 10 pins on his first roll.
// The frame is then completed with a single roll.
// The bonus for that frame is the value of the next 2 rolls.
//
// In the tenth frame a player who rolls a spare or strike is allowed to roll the extra balls to
// complete the frame. However no more than three balls can be rolled in tenth frame.
//
// Requirements
// Write a class Game that has two methods
@DisplayNameGeneration(ReplaceCamelCase.class)

// void roll(int pins) is called each time the player rolls a ball. The argument is the number of pins knocked down.
// int score() returns the total score for that game.
public class BowlingGameTest {
  BowlingGame bowlingGame = new BowlingGame(); // it's the same instance - WRONG!

  public BowlingGameTest() {
    System.out.println("New instance per each @Test!! > no need to clean instance variables between @Test");
  }

  @Test
  void initialScoreIs0() {
    int score = bowlingGame.score(); // "score" in the ubiquitous language of the teams
    assertThat(score).isEqualTo(0);
  }

  @Test
  void scoreIs1_givenRolled_1() {
    bowlingGame.roll(1);
    int score = bowlingGame.score();
    assertThat(score).isEqualTo(1);
  }

  // The game consists of 10 frames. In each frame the player has two rolls to knock down 10 pins.
  // The score for the frame is the total number of pins knocked down, plus bonuses for 'strikes' and 'spares'.
  // The score should be calculated at every point in game.

  @Test
  void scoreIs4_givenRolled_4() {
    bowlingGame.roll(4);
    int score = bowlingGame.score();
    assertThat(score).isEqualTo(4);
  }

  // in TDD a new test can be green from start for 3 reasons:
  // 1) the test contains a bug
  // 2) we are overlapping func we already implemented
  // 3) we might choose to leave that overlapping the test is a translation of an "Example" from the biz
  @Test
  // from the spec
  void scoreIs8_givenRolled_4_4() {
    bowlingGame.roll(4);
    bowlingGame.roll(4);
    int score = bowlingGame.score();
    assertThat(score).isEqualTo(8);
  }

  @Test
//  @DisplayName("good  quality code does not require //comments")
//  @DisplayName("good  quality test do not require @DisplayName")
  void scoreIsNegative_afterASpareBeforeTheNextRoll() {
    bowlingGame.roll(9);
    bowlingGame.roll(1);
    int score = bowlingGame.score();
    assertThat(score).isNegative();
  }

  @Test
  void addsTheNextRollAfterSpare() {
    bowlingGame.roll(9);
    bowlingGame.roll(1);
    bowlingGame.roll(1);
    int score = bowlingGame.score();
    final int BONUS = 1;
    assertThat(score).isEqualTo((10 + BONUS) + 1);
  }
  // comment out the last failing test, to start refactoring from GREEN
//  @Test
//  void normalGame() {
//    bowlingGame.roll(8);
//    bowlingGame.roll(1);
//    bowlingGame.roll(1);
//    int score = bowlingGame.score();
//    final int BONUS = 1;
//    assertThat(score).isEqualTo(10);
//  }


  // what if    BowlingGame.roll(0);
}
