package ro.victor.unittest.goldenmaster;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Random;

public class TriviaTest {
    public static void main(String[] args) throws IOException {

        Trivia aGame = new Trivia();
        StringWriter writer = new StringWriter();
        aGame.setWriter(writer);
        aGame.add("Chet");
        aGame.add("Pat");
        aGame.add("Sue");

//        for (int i = 0; i < 1000; i++) {

//        }
        Random rand = new Random(3);

        boolean notAWinner = false;
        do {
            aGame.roll(rand.nextInt(6) + 1); // [1 .. 6]

            if (rand.nextInt(9) == 0) { // 0 .. 8  ->  7;   p=0.125
                notAWinner = aGame.wrongAnswer();
            } else {
                notAWinner = aGame.wasCorrectlyAnswered();
            }

        } while (notAWinner);

        System.out.println(writer.toString());
    }
}
