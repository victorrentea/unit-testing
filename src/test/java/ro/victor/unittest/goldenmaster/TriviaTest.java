package ro.victor.unittest.goldenmaster;

import java.util.Random;

public class TriviaTest {
    public static void main(String[] args) {

        Trivia aGame = new Trivia();
        aGame.add("Chet");
        aGame.add("Pat");
        aGame.add("Sue");

        Random rand = new Random();
        boolean notAWinner = false;
        do {
            aGame.roll(rand.nextInt(5) + 1);

            if (rand.nextInt(9) == 7) {
                notAWinner = aGame.wrongAnswer();
            } else {
                notAWinner = aGame.wasCorrectlyAnswered();
            }

        } while (notAWinner);
    }
}
