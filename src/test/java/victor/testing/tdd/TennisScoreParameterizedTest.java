package victor.testing.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TennisScoreParameterizedTest {

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

  public static Object[][] testData() {
    return new Object[][] {
        {0, 0, "Love - Love"},
        {1, 0, "Fifteen - Love"},
        {2, 0, "Thirty - Love"},
        {3, 0, "Forty - Love"},
        {0, 1, "Love - Fifteen"},
        {0, 2, "Love - Thirty"},
        {0, 3, "Love - Forty"},
        {1, 1, "Fifteen - Fifteen"},
        {2, 2, "Thirty - Thirty"},
//        {3, 3, "Deuce"},
//        {4, 3, "Advantage Player1"},
//        {3, 4, "Advantage Player2"},
//        {4, 4, "Deuce"},
//        {5, 4, "Advantage Player1"},
//        {4, 5, "Advantage Player2"},
        {6, 4, "Game won Player1"},
//        {4, 6, "Game won Player2"},
    };
  }

  @ParameterizedTest(name = "Player1: {0}, Player2: {1} => {2}")
  @MethodSource("testData")
  void parameterizedTest(int player1Points, int player2Points, String expectedScore) {
    setPoints(player1Points, player2Points);

    String actual = tennisScore.getScore();

    assertEquals(expectedScore, actual);
  }
}
