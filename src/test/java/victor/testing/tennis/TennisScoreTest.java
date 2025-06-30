package victor.testing.tennis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static victor.testing.tennis.TennisScore.Player.ONE;
import static victor.testing.tennis.TennisScore.Player.TWO;

// The tennis society has contracted you to build a scoreboard to display
// the current score during tennis games. Rules:
// "<player1>-<player2>"

//1) A game is won by the first player to have won at least four points in total
// and at least two points more than the opponent.

//2) ✅scores from zero to three points are described as ⭐️<start with easiest rule
// “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.

//3) If at least three points have been scored by each player,
// and the scores are EQUAL, the score is “Deuce”.

//4) If at least three points have been scored by each side
// and a player has one more point than his opponent,
// the score of the game is “Advantage” for the player in the lead.

public class TennisScoreTest {
  TennisScore tennisScore = new TennisScore();


  @Test
  void love_scoreIsZero() {
    String scoreDescription = tennisScore.getScoreDescription();

    assertThat(scoreDescription).isEqualTo("Love-Love");
  }

  @Test
  void fifteen_player1scoreIsOne() {
    tennisScore.addAPointToPlayersId(ONE);
    String scoreDescription = tennisScore.getScoreDescription();

    assertThat(scoreDescription).isEqualTo("Fifteen-Love");
  }

  @Test
  void fifteen_player2scoreIsOne() {
    tennisScore.addAPointToPlayersId(TWO);
    String scoreDescription = tennisScore.getScoreDescription();

    assertThat(scoreDescription).isEqualTo("Love-Fifteen");
  }

  @Test
  void thirty_player1scoreIsTwo() {
    tennisScore.addAPointToPlayersId(ONE);
    tennisScore.addAPointToPlayersId(ONE);
    String scoreDescription = tennisScore.getScoreDescription();

    assertThat(scoreDescription).isEqualTo("Thirty-Love");
  }

     static Stream<TestCase> testData() {
      return of(
          new TestCase(1,0,"Fifteen-Love"),
          new TestCase(0,1,"Love-Fifteen"),
          new TestCase(2,0,"Thirty-Love"),
          new TestCase(3,0,"Forty-Love")

      );
    }

    record TestCase(int player1, int player2, String expectedResult){}

//  @CsvSource({
//      "1,0,Fifteen-Love",
//      "2,0,Thirty-Love",
//  })
  @MethodSource("testData")
  @ParameterizedTest
  void paramTest(TestCase testCase) {
    for (int i = 0; i < testCase.player1; i++) {
      tennisScore.addAPointToPlayersId(ONE);
    }
    for (int i = 0; i < testCase.player2; i++) {
      tennisScore.addAPointToPlayersId(TWO);
    }
    String scoreDescription = tennisScore.getScoreDescription();

    assertThat(scoreDescription).isEqualTo(testCase.expectedResult);
  }

}
