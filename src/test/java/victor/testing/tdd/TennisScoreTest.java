package victor.testing.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    String actual = tennisScore.getScore();

    assertEquals("Love - Love", actual);
  }

  @Test
  void scoreIsFifteenLove_whenPlayer1Scored1Point() {
    tennisScore.addPointToPlayer1();

    String actual = tennisScore.getScore();

    assertEquals("Fifteen - Love", actual);
  }

  @Test
  void scoreIsThirtyLove_whenPlayer1Scored2Point() {
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();

    String actual = tennisScore.getScore();

    assertEquals("Thirty - Love", actual);
  }

  @Test
  void scoreIsFortyLove_whenPlayer1Scored3Point() {
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();

    String actual = tennisScore.getScore();

    assertEquals("Forty - Love", actual);
  }

  @Test
  void scoreIsLoveFifteen_whenPlayer2Scored1Point() {
    tennisScore.addPointToPlayer2();

    String actual = tennisScore.getScore();

    assertEquals("Love - Fifteen", actual);
  }

  @Test
  void scoreIsThirtyForty_whenPlayer1Scored2PointsAndPlayer2Scored3Points() {
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer1();
    tennisScore.addPointToPlayer2();
    tennisScore.addPointToPlayer2();
    tennisScore.addPointToPlayer2();

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
    // biz says it is NOT my code's responsibility to validate its inputs.

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
