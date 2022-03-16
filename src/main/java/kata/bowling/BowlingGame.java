package kata.bowling;

import java.util.ArrayList;
import java.util.List;

class Frame {
   private final int roll1;
   private final int roll2;

   Frame(int roll1, int roll2) {
      this.roll1 = roll1;
      this.roll2 = roll2;
   }

   public int getRoll2() {
      return roll2;
   }

   public int baseScore() {
      return roll1 + roll2;
   }

   public boolean isSpare() {
      return roll1 + roll2 == 10 && roll2 != 0;
   }
   public boolean isStrike() {
      return roll1== 10 && roll2 == 0;
   }

   public int getRoll1() {
      return roll1;
   }
}

public class BowlingGame {
   private final List<Integer> rolls = new ArrayList<>();

   public void roll(int pins) {
      if (pins > 10 || pins < 0) {
         throw new IllegalArgumentException();
      }
      rolls.add(pins);
   }

   public int score() {
      List<Frame> frames = buildFrames();

      int baseScore = frames.stream().mapToInt(Frame::baseScore).sum();

      int bonus = 0;
      for (int i = 0; i < frames.size() -1 ; i++) {
         Frame frame = frames.get(i);
         if (frame.isSpare()) {
            bonus += frames.get(i + 1).getRoll1();
         }
         if (frame.isStrike()) {
            bonus += frames.get(i + 1).getRoll1();
            bonus += frames.get(i + 1).getRoll2();
         }
      }

      return baseScore + bonus;
   }

   private List<Frame> buildFrames() {
      List<Frame> frames = new ArrayList<>();
      for (int i = 0; i <rolls.size() ;i++) {
         if (rolls.get(i) == 10) {
            frames.add(new Frame(10 , 0));
         } else {
            frames.add(new Frame(rolls.get(i), rolls.get(i + 1)));
            i++;
         }
      }
      return frames;
   }
}
