package victor.testing.tdd;

import org.hibernate.annotations.Source;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import victor.testing.tools.HumanReadableTestNames;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayNameGeneration(HumanReadableTestNames.class)
public class TennisScoreParameterizedTest {
  private TennisScore tennisScore  = new TennisScore();

  public TennisScoreParameterizedTest() {
    System.out.println("Cate o instanta noua de clasa de test / @Test");
  }

  // se preteaza pentru algoritmi complicati in care tot ce difera intre cazurile de test sunt doar date, coeficienti
  //  => ff frecvent si BIZU a avut nevoie sa explice alg cu un tabel/ multe exmple.

  //
  public static List<Arguments> combinatii() {
    return List.of(
            arguments(0,0, "Love-Love"),
            arguments(1,0, "Fifteen-Love"),
            arguments(7,7, "Deuce"),
            arguments(7,8, "Advantage")
    );
  }
  
  @ParameterizedTest
  @MethodSource("combinatii")
  void advantagePlayer1(int player1Points, int player2Points, String expectedScore) {
    winPoints(player1Points, Player.ONE);
    winPoints(player2Points, Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo(expectedScore);
  }


  // testing DSL = Domain Specific Language = miniframework
  // un mic helper function pentru a-mi putea testa mai usor codul de prod
  private void winPoints(int x, Player two) {
    for (int i = 0; i < x; i++) {
      tennisScore.winsPoint(two);
    }
  }
}
