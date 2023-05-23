package victor.testing.tdd.tennis;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

public class TennisGameParameterizedTest {
  TennisGame tennisGame = new TennisGame();

  static Stream<Arguments> data() {
    return Stream.of(
//        of(3, 0,  0,  0,  0,  0,  0, "Forty-Love", false, true, false),
        of(3, 2, "Forty-Thirty"),
        of(0, 2, "Love-Thirty"),
        of(3, 3, "Deuce"),
        of(4, 4, "Deuce"),
        of(7, 6, "Advantage")
    );
  }
  // Use this when your tests have the same structure
  // and the only thing that is different are some literals (int,strings...)

  // when you can reduce the tests to an EXCEL file.

  @ParameterizedTest(name = "When first team scored {0} points, and second team scored {1}, then the score is ''{2}''")
  @MethodSource("data")
//  @CsvSource(textBlock = """
//      3,0,Forty-Love
//      """)
//  void aSingleTest(int firstTeamPoints,int firstTeamPoints,int firstTeamPoints,int firstTeamPoints,int firstTeamPoints,int firstTeamPoints,int firstTeamPoints, int secondTeamPoints, String expectedScore
  void aSingleTest(int firstTeamPoints, int secondTeamPoints, String expectedScore) {
    setPoints(firstTeamPoints, secondTeamPoints);
    String actual = tennisGame.getScore();
    assertThat(actual).isEqualTo(expectedScore);
//    if (param4) {
//      code should throw ...
//    }
//    if (param5) {
//      verify(senderMock).sendMessage()
//    }
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
