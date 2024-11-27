package victor.testing.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import victor.testing.tennis.TennisScore;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@RunWith(SpringRunner.class)
//@SpringBootTest

@RunWith(Parameterized.class)
// in charge of instantiating and configuring the test instance

// in Junit5 -> @ExtendWith(ParameterizedTestExtension::class)
public class ParameterizedTests {
  private final TennisScore score = new TennisScore();

  private final int player1Points;
  private final int player2Points;
  private final String expectedScore;

  public ParameterizedTests(int player1Points, int player2Points, String expectedScore) {
    this.player1Points = player1Points;
    this.player2Points = player2Points;
    this.expectedScore = expectedScore;
  }

  @Parameterized.Parameters(name = "{0} - {1} => {2}")
  public static Object[][] data() {
    return new Object[][] {
        {0, 0, "Love-Love"},
        {1, 0, "Fifteen-Love"},
        {2, 0, "Thirty-Love"},
        {3, 0, "Forty-Love"},
        {0, 1, "Love-Fifteen"},
        {0, 2, "Love-Thirty"},
        {0, 3, "Love-Forty"},
        {1, 1, "Fifteen-Fifteen"},
        {2, 2, "Thirty-Thirty"},
        {3, 3, "Deuce"},
        {4, 4, "Deuce"},
        {5, 5, "Deuce"},
        {4, 3, "Advantage Player1"},
        {3, 4, "Advantage Player2"},
        {4, 0, "Game won Player1"},
        {0, 4, "Game won Player2"},
        {4, 2, "Game won Player1"},
        {2, 4, "Game won Player2"},
        {4, 5, "Advantage Player2"},
        {5, 4, "Advantage Player1"},
    };
  }


  @Test
  public void oneTest() {
    for (int i = 0; i < player1Points; i++) {
      score.addPoint(1);
    }
    for (int i = 0; i < player2Points; i++) {
      score.addPoint(2);
    }
    assertEquals(expectedScore, score.score());
  }

}
