package ro.victor.unittest.goldenmaster;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Random;

public class TriviaTest {
    @Test
    public void zatest() {
        for (int seed = 0; seed < 1000; seed++) {

            String expected = runGameAsPureFunction(new Trivia(), seed);

            String actual = runGameAsPureFunction(new TriviaRefactored(), seed);

            Assert.assertEquals(expected, actual);
        }

    }

    private static String runGameAsPureFunction(ITrivia aGame, int seed) {
        Random rand = new Random(seed);
        StringWriter writer = new StringWriter();
        aGame.setWriter(writer);
        aGame.add("Chet");
        aGame.add("Pat");
        aGame.add("Sue");
        boolean notAWinner = false;
        do {
            aGame.roll(rand.nextInt(6) + 1); // [1 .. 6]

            if (rand.nextInt(9) == 0) { // 0 .. 8  ->  7;   p=0.125
                notAWinner = aGame.wrongAnswer();
            } else {
                notAWinner = aGame.wasCorrectlyAnswered();
            }

        } while (notAWinner);
        return writer.toString();
    }
}
