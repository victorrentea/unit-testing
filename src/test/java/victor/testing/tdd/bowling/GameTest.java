package victor.testing.tdd.bowling;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The game consists of 10 frames.
 * In each frame the player has two rolls to knock down 10 pins.

 * The score for the frame is the total number of pins knocked down,
 * plus bonuses for strikes and spares.
 *
 * A spare is when the player knocks down all 10 pins in two rolls.
 * The bonus for that frame is the number of pins knocked down by the next roll.
 *
 * A strike is when the player knocks down all 10 pins on his first roll.
 * The frame is then completed with a single roll. The bonus for that frame is the value of the next TWO rolls.
 *
 * In the tenth frame a player who rolls a spare or strike is allowed to roll the extra balls to complete the frame.
 * However no more than three balls can be rolled in tenth frame.
 *
 */

public class GameTest {
    Game game = new Game();
    @Test
    void beginnersGame() {
        rollZeros(20);

        assertThat(game.score()).isEqualTo(0);
    }
    @Test
    void returnsSumOfRolls() {
        game.roll(1);
        game.roll(1);
        rollZeros(18);

        assertThat(game.score()).isEqualTo(2);
    }
    @Test
    void spare() {
        final int ROLL_AFTER_SPARE = 1;
        game.roll(5);
        game.roll(5);
        game.roll(ROLL_AFTER_SPARE);
        rollZeros(17);

        assertThat(game.score()).isEqualTo(10 + ROLL_AFTER_SPARE + ROLL_AFTER_SPARE);
    }
    @Test
    void notASpare() {
        final int ROLL_AFTER_SPARE = 1;
        game.roll(0);
        game.roll(5);
        game.roll(5);
        game.roll(1);
        rollZeros(16);

        assertThat(game.score()).isEqualTo(11);
    }

    @Test
    @Disabled
    void strike() {
        final int ROLL_1_AFTER = 1;
        final int ROLL_2_AFTER = 2;

        game.roll(10); // 1st frame

        game.roll(ROLL_1_AFTER); // 2nd
        game.roll(ROLL_2_AFTER); // 2nd

        rollZeros(16);

        int bonus = ROLL_1_AFTER + ROLL_2_AFTER;
        int nextFrameRegularScore = ROLL_1_AFTER + ROLL_2_AFTER;
        assertThat(game.score()).isEqualTo(10 + bonus + nextFrameRegularScore);
    }

    private void rollZeros(int x) {
        for (int i = 0; i < x; i++) {
            game.roll(0);
        }
    }
//    @Test
//    @Disabled("not there yet")
//    void justOneSpare() { // 5 5 5 1 1 1 1 ...
//        final int SPARE_BONUS = 5;
//        game.roll(5);
//        game.roll(5);
//        game.roll(5);
//        for (int i = 0; i < 17; i++) {
//            game.roll(1);
//        } // we can focus the test better.
//
//        assertThat(game.score()).isEqualTo(15 + 17 + SPARE_BONUS);
//    }
}
