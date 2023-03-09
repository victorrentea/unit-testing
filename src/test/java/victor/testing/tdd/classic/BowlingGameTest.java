package victor.testing.tdd.classic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
  void scoreIsOne_givenRolled_1() {
    bowlingGame.roll(1);
    int score = bowlingGame.score(); // "score" in the ubiquitous language of the teams
    assertThat(score).isEqualTo(1);
  }

  // The game consists of 10 frames. In each frame the player has two rolls to knock down 10 pins.
  // The score for the frame is the total number of pins knocked down, plus bonuses for 'strikes' and 'spares'.
  // The score should be calculated at every point in game.

  @Test
  void scoreIsOne_givenRolled_1_2() {
    bowlingGame.roll(1);
    bowlingGame.roll(2);
    int score = bowlingGame.score(); // "score" in the ubiquitous language of the teams
    assertThat(score).isEqualTo(2);
  }

// what if    BowlingGame.roll(0);
}
