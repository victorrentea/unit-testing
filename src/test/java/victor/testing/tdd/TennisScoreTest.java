package victor.testing.tdd;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import victor.testing.tools.HumanReadableTestNames;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(HumanReadableTestNames.class)
public class TennisScoreTest {
  private TennisScore tennisScore  = new TennisScore();

  public TennisScoreTest() {
    System.out.println("Cate o instanta noua de clasa de test / @Test");
  }

  // The running score of each game is described in a manner peculiar to tennis:
  // scores from zero to three points
  // are described as “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.

//  @BeforeEach // @Before in 4
//  final void before() {
//    tennisScore = new TennisScore();
//  }
  @Test
  void zeroZero() {
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Love-Love");
  }

    // this test was leaking data -> testele de dupa vad datele ramase
    // - IN DB (daca e DB in-memory/in-docker) < nu la noi ca noi Mock repository
    // - stare (campuri) pe singletoane Spring -> @DirtiesContext (de evitate)
    // - campuri static
  @Test
//  void whenPlayer1Scores_thenTheScoreIsFifteenLove() {
  void fifteenLove_whenPlayer1Scores() {
    tennisScore.winsPoint(Player.ONE);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Fifteen-Love");
  }



  @Test
  void thirtyLove_whenPlayer1Scores2Points() {
    tennisScore.winsPoint(Player.ONE);
    tennisScore.winsPoint(Player.ONE);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Thirty-Love");
  }
  // overlapping, doar ca poti sa-l lasi totusi in cod daca este traducerea a vreunui exemplu
  // gasit prin spec.
  @Test
  void fifteenThirty() {
    tennisScore.winsPoint(Player.ONE);
    tennisScore.winsPoint(Player.TWO);
    tennisScore.winsPoint(Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Fifteen-Thirty");
  }
  @Test
  void treiZero() {
    tennisScore.winsPoint(Player.ONE);
    tennisScore.winsPoint(Player.ONE);
    tennisScore.winsPoint(Player.ONE);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Forty-Love");
  }
  @Test
  void zeroUnu() {
    tennisScore.winsPoint(Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Love-Fifteen");
  }

  //If at least three points have been scored by each player,
  // and the scores are equal, the score is “Deuce”.
  @Test
  void deuce() {
    winPoints(3, Player.ONE);
    winPoints(3, Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Deuce");
  }

  @Test
  void deuce4() {
    winPoints(4, Player.ONE);
    winPoints(4, Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Deuce");
  }

  //If at least three points have been scored by each side
  // and a player has one more point than his opponent,
  // the score of the game is “Advantage” for the
  // player in the lead.

  @Test
  void advantagePlayer1() {
    winPoints(4, Player.ONE);
    winPoints(3, Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Advantage");
  }
  @Test
  void advantagePlayer2() {
    winPoints(3, Player.ONE);
    winPoints(4, Player.TWO);
    String score = tennisScore.getScore();
    assertThat(score).isEqualTo("Advantage");
  }


  // testing DSL = Domain Specific Language = miniframework
  // un mic helper function pentru a-mi putea testa mai usor codul de prod
  private void winPoints(int x, Player two) {
    for (int i = 0; i < x; i++) {
      tennisScore.winsPoint(two);
    }
  }
}
