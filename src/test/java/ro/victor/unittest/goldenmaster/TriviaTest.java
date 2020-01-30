package ro.victor.unittest.goldenmaster;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class TriviaTest {
    @Test
    public void caracterizationTest() {
        for (int seed = 0; seed < 10_000; seed++) {
            String expectedOutput = getOutput(seed, new Trivia());
            String actualOutput = getOutput(seed, new TriviaBetter());
            assertEquals(expectedOutput, actualOutput);
        }
    }
    private static String getOutput(int seed, ITrivia trivia) {
        Random rand = new Random(seed);
        StringWriter sw = new StringWriter();
        trivia.setWriter(sw);
        int nPlayers = rand.nextInt(4) + 2;
        for (int i = 0; i < nPlayers; i++) {
            trivia.add("Player" + i);
        }


        // f (trivia, seed) : String

        boolean notAWinner = false;
        do {
            trivia.roll(rand.nextInt(5) + 1); // 1-- 5

            if (rand.nextInt(9) == 7) {
                notAWinner = trivia.wrongAnswer();
            } else {
                notAWinner = trivia.wasCorrectlyAnswered();
            }

        } while (notAWinner);


        return sw.toString();
    }
}
