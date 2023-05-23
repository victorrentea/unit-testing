package victor.testing.tdd.tennis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameParameterizedTest {
  TennisGame tennisGame = new TennisGame();

  static Stream<Arguments> data() {
    return Stream.of(
        Arguments.of(3,0,"Forty-Love"),
        Arguments.of(3,2,"Forty-Thirty"),
        Arguments.of(0,2,"Love-Thirty"),
        Arguments.of(3,3,"Deuce"),
        Arguments.of(4,4,"Deuce"),
        Arguments.of(7,6,"Advantage")
    );
  }
  
  @ParameterizedTest(name = "When first team scored {0} points, and second team scored {1}, then the score is ''{2}''")
  @MethodSource("data")
  void aSingleTest(int firstTeamPoints, int secondTeamPoints, String expectedScore) {
    setPoints(firstTeamPoints, secondTeamPoints);
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo(expectedScore);
  }

  // test helper method, to work with an unfriendly testing api
  private void setPoints(int firstTeamPoints, int secondTeamPoints) {
    for (int i = 0; i < firstTeamPoints; i++) {
      tennisGame.addPointToFirstTeam();
    }
    for (int i = 0; i < secondTeamPoints; i++) {
      tennisGame.addPointToSecondTeam();
    }
  }

}