package ro.victor.unittest.goldenmaster;

import java.util.Random;

public class TriviaTest {
    public static void main(String[] args) {

        Trivia aGame = new Trivia();
        aGame.add("Chet");
        aGame.add("Pat");
        aGame.add("Sue");

//        aGame.roll(5);
//        aGame.wasCorrectlyAnswered();
//        aGame.roll(4);
//        aGame.wrongAnswer();

        Random rand = new Random(1);


        boolean notAWinner = false;
        do {
            aGame.roll(rand.nextInt(6) + 1); // [1 .. 6]

            if (rand.nextInt(9) == 0) { // 0 .. 8  ->  7;   p=0.125
                notAWinner = aGame.wrongAnswer();
            } else {
                notAWinner = aGame.wasCorrectlyAnswered();
            }

        } while (notAWinner);
    }
}
