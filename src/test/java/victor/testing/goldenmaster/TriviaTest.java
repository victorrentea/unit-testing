package victor.testing.goldenmaster;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class TriviaTest {

    @Test
    public void test() {
//        long seed = 13L;
        for (int seed = 0; seed < 10_000; seed++) {

        List<String> expectedOutput = runGame(new TriviaOriginal(), seed);
        List<String> actualOutput = runGame(new TriviaRescris(), seed);

        assertEquals(
            String.join("\n",expectedOutput),
            String.join("\n",actualOutput));

        }
    }

    private static List<String> runGame(ITrivia aGame, long seed) {
        List<String> output = new ArrayList<>();
        aGame.setWriterFunction__dinTeste(obj -> {
            output.add("" + obj);
        });
        // 5 ani merge struna. cust sunt ferici. doar ca are 35.000 de LOC. ce ii faci?
        aGame.add("Chet");
        aGame.add("Pat");
        aGame.add("Sue");

        Random rand = new Random(seed);
        boolean notAWinner = false;
        do {
            aGame.roll(rand.nextInt(5) + 1);

            if (rand.nextInt(9) == 7) {
                notAWinner = aGame.wrongAnswer();
            } else {
                notAWinner = aGame.wasCorrectlyAnswered();
            }

        } while (notAWinner);
        return output;
    }
}

