package victor.testing.tdd.classic;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
// 1) int[][] matrixBro;
// 2) flags (booleans)
// 3) List<Frame>
  // * no partial Frame in that list (the numbers, no bonuses)
  // the score() will compute bonuses, as they involve multiple Frames.

  // int grandTotal;
//  int counter ; [0..2] no of roll in frame; when 2, we add the frameScore
  // int frameScore; [0..10]

@Slf4j
public class BowlingGame {
  private Integer firstRollInCurrentFrame;

  record Frame(int first, int second) {
    boolean isSpare() {
      return first + second == 10;
    }
    int totalPins() {
      return first + second;
    }
  }

  private final List<Frame> completedFrames = new ArrayList<>();


  public int score() {
    int totalScore = completedFrames.stream()
            .mapToInt(Frame::totalPins).sum();
    log.info("Total of frames :" +totalScore);
    if (firstRollInCurrentFrame != null) {
      if (!completedFrames.isEmpty()) {
        if (completedFrames.get(0).isSpare()) {
          totalScore += firstRollInCurrentFrame;
        }
      }
    }
    log.info("Total after Bonuses :" +totalScore);
    if (firstRollInCurrentFrame != null) {
      totalScore += firstRollInCurrentFrame;
    }
    log.info("Total including the current ongoing frame {}: " + totalScore, firstRollInCurrentFrame);
    if (!completedFrames.isEmpty()) {
      Frame lastFrame = completedFrames.get(completedFrames.size() - 1);
      if (lastFrame.isSpare() && firstRollInCurrentFrame == null) {
        return -1;
      }
    }
    return totalScore;
  }

  public void roll(int pins) {
    if (firstRollInCurrentFrame == null) {
      firstRollInCurrentFrame = pins;
    } else {
      completedFrames.add(new Frame(firstRollInCurrentFrame, pins));
      firstRollInCurrentFrame = null;
    }
  }
}
