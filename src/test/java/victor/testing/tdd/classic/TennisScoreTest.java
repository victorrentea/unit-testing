package victor.testing.tdd.classic;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreTest {

    TennisScore tennisScore = new TennisScore();

    @Test
    void loveLove_forNewGame() {
        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Love - Love");
    }

    @Test
    void fifteenLove_whenPlayer1Scores1Point() {
        tennisScore.addPoint(Player.ONE); // cum pasezi ideea de "jucatorul 1": 1, 0, enum, ??
        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Fifteen - Love");
    }

    @Test
    void loveFifteen_whenPlayer2Scores1Point() {
        tennisScore.addPoint(Player.TWO);
        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Love - Fifteen");
    }

    // The running score of each game is described in a manner peculiar to tennis:
    // scores from zero to three points are described as
    // “Love”, “Fifteen”, “Thirty”, and “Forty” respectively.
    @Test
    void fifteenFifteen() { //test overlapping
        tennisScore.addPoint(Player.ONE);
        tennisScore.addPoint(Player.TWO);
        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Fifteen - Fifteen");
    }

    @Test
    void thirtyLove() {
        tennisScore.addPoint(Player.ONE); // cum pasezi ideea de "jucatorul 1": 1, 0, enum, ??
        tennisScore.addPoint(Player.ONE); // cum pasezi ideea de "jucatorul 1": 1, 0, enum, ??
        String score = tennisScore.getScore();

        assertThat(score).isEqualTo("Thirty - Love");
    }

}

// 1> te apuci de partea din req cea mai simpla
// 2> testele in fata, >>> multe intrebari >> clarifica API
// 3> cand un test e rosu > fixezi cat de repede poti (STUPID)
// 4> ca sa pui mai multa logica, pui test intai
// 5> NU AI VOIE sa adaugi nici o linie de cod in prod decat pentru a face sa treaca un test
// 6> KISS (Code Golf)> repara testul cat mai repede
// 7> daca vezi teste care pica doar cand le rulezi in suita cu fratii dar trec cand le rulezi singure => cuplari intre teste.
//      Testele nu sunt izolate => date care "curg" dintr-unu-n altu'
// 8> Nu ai voie refactoring daca ai teste picate. Refactoringul pleaca si se termina pe verde.
// 9> cand fixezi testul e acceptabil sa faci copy-paste, si alte abominatii.
// 10> scriu un test NOU si e gata verde:
//   1) esti prost: nu te-ai prins ca deja implementasesi featureul
//   2) esti prost: testul nou scris ARE UN BUG: copy-paste din alt test sau din prod
//   3) Lasi teste overlapping OK daca traduci exemple concrete mentionate in requirementuri
// 11> TDD iti da libertatea sa te joci fara sa-ti fie frica. Te incurajeaza.

// ** FACI TDD DE FAPT NU PENTRU SEFU. CI PENTRU TINE> CA SA CRESTI. sa stai mai mult timp in refactoring,> experimentand mai multe idei > devii mai destept > $++

// Junit ruleaza testele tale alandala. nu in ordinea in care le-ai scris. Pentru ca sa caute cuplari intre teste.