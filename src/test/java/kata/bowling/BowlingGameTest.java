package kata.bowling;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BowlingGameTest {
   private BowlingGame game = new BowlingGame();

   @Test
   void worstGame() {
      rollZeros(20);

      int score = game.score();

      assertThat(score).isEqualTo(0);
   }

   @Test
   void returnsOne_forOnePinDown() {
      game.roll(1);

      rollZeros(19);

      int score = game.score();

      assertThat(score).isEqualTo(1);
   }

   @Test
   void throwsForTooLargeRoll() {
      assertThatThrownBy(() -> game.roll(11))
          .isInstanceOf(IllegalArgumentException.class);
   }

   @Test
   void throwsForNegativeRoll() {
      assertThatThrownBy(() -> game.roll(-1))
          .isInstanceOf(IllegalArgumentException.class);
   }

   @Test
   void spare() {
      roll(5, 5, 1);
      rollZeros(17);

      int score = game.score();

      assertThat(score).isEqualTo(11 + 1);
   }
   @Test
   void twoSpares() {
      roll(5, 5, 5, 5, 1);
      rollZeros(15);

      int score = game.score();

      assertThat(score).isEqualTo(15 + 11 + 1);
   }

   @Test
   void nonSpare() {
      roll(0, 5, 5, 1);
      rollZeros(16);

      assertThat(game.score()).isEqualTo(11);
   }

   @Test
   void strike() {
      roll(10, 1, 1);
      rollZeros(16);

      int score = game.score();

      assertThat(score).isEqualTo(12 + 2);
   }
   @Test
   void godGame() {
      for (int i = 0; i < 12; i++) {
         game.roll(10);
      }

      int score = game.score();

      assertThat(score).isEqualTo(300);
   }

   private void roll(int... pinsArray) {
      for (int pins : pinsArray) {
         game.roll(pins);
      }
   }

   private void rollZeros(int numberOfRoll) {
      for (int i = 0; i < numberOfRoll; i++) {
         game.roll(0);
      }
   }
}
