package victor.testing.tdd.bowling;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {

    private final List<Frame> frames = new ArrayList<>();

    public void roll(int pins) {
        if (getLastFrame() != null && !getLastFrame().isComplete() ) {
            getLastFrame().setRoll2(pins);
        } else {
            frames.add(new Frame(pins));
        }
     }

    @Nullable
    private Frame getLastFrame() {
        return frames.isEmpty() ? null : frames.get(frames.size() - 1);
    }

    public int score() {
        int bonusForSpares = bonusForSpares();

        int total = frames.stream().mapToInt(Frame::totalPins).sum();
        return bonusForSpares + total;
    }

    private int bonusForSpares() {
        int bonusForSpares = 0;
        for (int i = 0; i < frames.size(); i++) {
            if ( frames.get(i).isSpare()) {
                bonusForSpares += frames.get(i+1).getRoll1();
            }
        }
        return bonusForSpares;
    }
}

// if Frame were part of the API, then make it immutable.
//record Frame(int roll1, Optional<Integer> roll2, Optional<Integer> roll3){}

class Frame{
    private final int roll1;
    private Integer roll2;

    Frame(int roll1) {
        this.roll1 = roll1;
    }

    public void setRoll2(Integer roll2) {
        this.roll2 = roll2;
    }

    public int getRoll1() {
        return roll1;
    }

    public boolean isComplete() {
        return roll1 == 10 || roll2 != null;
    }

    public int totalPins() {
        return roll1 + (roll2!= null ? roll2 : 0);
    }

    public boolean isSpare() {
        return roll2 != null && roll1 + roll2 == 10;
    }
}