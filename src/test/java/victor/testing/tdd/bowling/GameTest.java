package victor.testing.tdd.bowling;

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
 * The frame is then completed with a single roll. The bonus for that frame is the value of the next two rolls.
 *
 * In the tenth frame a player who rolls a spare or strike is allowed to roll the extra balls to complete the frame. However no more than three balls can be rolled in tenth frame.
 *
 */

public class GameTest {
    Game game = new Game();
    @Test
    void beginnersGame() {
        for (int i = 0; i < 20; i++) {
            game.roll(0);
        }

        assertThat(game.score()).isEqualTo(0);
    }
    @Test
    void returns20_whenRolls1EachTime() {
        for (int i = 0; i < 20; i++) {
            game.roll(1);
        }

        assertThat(game.score()).isEqualTo(20);
    }
    @Test
    void spare() { // 5 5 5 1 1 ...
        final int SPARE_BONUS = 1;
        game.roll(5);
        game.roll(5);
        for (int i = 0; i < 18; i++) {
            game.roll(1);
        } // we can focus the test better ("test less")

        assertThat(game.score()).isEqualTo(10 + 18 + SPARE_BONUS);
    }
    @Test
    void justOneSpare() { // 5 5 5 1 1 1 1 ...
        final int SPARE_BONUS = 5;
        game.roll(5);
        game.roll(5);
        game.roll(5);
        for (int i = 0; i < 17; i++) {
            game.roll(1);
        } // we can focus the test better.

        assertThat(game.score()).isEqualTo(15 + 17 + SPARE_BONUS);
    }
}
