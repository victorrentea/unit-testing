package victor.testing.tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import victor.testing.tools.HumanReadableTestNames;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(HumanReadableTestNames.class)
public class TennisScoreTest {

  private TennisScore tennisScore = new TennisScore();

  // test-helper method
  private void setPoints(int player1Points, int player2Points) {
    for (int i = 0; i < player1Points; i++) {
      tennisScore.addPointToPlayer1();
    }
    for (int i = 0; i < player2Points; i++) {
      tennisScore.addPointToPlayer2();
    }
  }

  @Test
  void newGame() {
    setPoints(0, 0);

    String actual = tennisScore.getScore();

    assertEquals("Love - All", actual);
  }

  @Test
  void scoreIsFifteenLove_whenPlayer1Scored1Point() {
    setPoints(1, 0);

    String actual = tennisScore.getScore();

    assertEquals("Fifteen - Love", actual);
  }

  @Test
    // risk=can get out of sync just like  // comments in code can
  @DisplayName("Score is Thirty - Love when Player1 scored 2 points")
  void scoreIsThirtyLove_whenPlayer1Scored2Point() {
    setPoints(2, 0);

    String actual = tennisScore.getScore();

    assertEquals("Thirty - Love", actual);
  }

  @Test
  void scoreIsFortyLove_whenPlayer1Scored3Point() {
    setPoints(3, 0);

    String actual = tennisScore.getScore();

    assertEquals("Forty - Love", actual);
  }

  @Test
  void scoreIsLoveFifteen_whenPlayer2Scored1Point() {
//    tennisScore.addPointToPlayer2();
    setPoints(0, 1);

    String actual = tennisScore.getScore();

    assertEquals("Love - Fifteen", actual);
  }

  @Test
  void scoreIsThirtyForty_whenPlayer1Scored2PointsAndPlayer2Scored3Points() {
    setPoints(2, 3);

    String actual = tennisScore.getScore();

    // example from the spec
    assertEquals("Thirty - Forty", actual);
  }
  // A game is won by the first player to have won at least four points in total and
  //   at least two points more than the opponent.

  @Test
  void player1WonGame_whenScoreIs4_2() {
//    tennisScore.addPointToPlayer1();
//    tennisScore.addPointToPlayer1();
//    tennisScore.addPointToPlayer1();
//    tennisScore.addPointToPlayer2();
//    tennisScore.addPointToPlayer2();
//    tennisScore.addPointToPlayer1();
    // gameSequence(1, 1, 1, 2, 2, 1);
//    tennisScore.setScore(4, 2); // NOT a valid business flow. just for testing
    setPoints(4, 2);

    String actual = tennisScore.getScore();

    assertEquals("Game won Player1", actual);
  }

  // 4-0 => Game Won
  // 4-3 => Advantage
  // 4-4 => Deuce
  // 5-0 => illegal


//  Postpone
//  @Test
  void outOfBounds() {
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();

    assertThrows(IllegalStateException.class, () -> tennisScore.getScore());
  }


  // Fifteen - Fifteen


}

// TODO test ALL COMBINATIONS BECAUSE IT'S SUPER CRITICAL LOGIC
//  these combinations actually got to us in the spec.
//  RULE: any "example" provided by BA/PO in the spec SHOULD BE translated in a test.
