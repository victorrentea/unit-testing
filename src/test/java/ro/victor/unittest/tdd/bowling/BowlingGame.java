package ro.victor.unittest.tdd.bowling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

abstract class Frame {
    private final int pins;
    private Frame next;

    protected Frame(int pins) {
        this.pins = pins;
    }

    public int pins() {
        return pins;
    }

    public void setNext(Frame next) {
        this.next = next;
    }

    public Optional<Frame> getNext() {
        return ofNullable(next);
    }

    abstract int score();
}

class RegularFrame extends Frame {
    public RegularFrame(int pins) {
        super(pins);
    }
    int score() {
        return pins();
    }
}

class Spare extends Frame {
    public Spare() {
        super(10);
    }
    int score() {
        return 10 + getNext().map(Frame::pins).orElse(0);
    }
}

class Strike extends Frame {
    public Strike() {
        super(10);
    }
    int score() {
        return 10 +
                getNext().map(Frame::pins).orElse(0) +
                getNext().flatMap(Frame::getNext).map(Frame::pins).orElse(0);
    }
}


public class BowlingGame {
    public static int calculateScore(List<Integer> row) {
        int sum = row.stream().mapToInt(i -> i).sum();

        List<Frame> frames = new ArrayList<>();

        while (!row.isEmpty()) {
            if (row.get(0) == 10) {
                frames.add(new Strike());
                row.remove(0);
            } else if (row.get(0) + row.get(1) == 10) {
                frames.add(new Spare());
                row.remove(0);
                row.remove(0);
            } else {
                frames.add(new RegularFrame(row.get(0) + row.get(1)));
                row.remove(0);
                row.remove(0);
            }
        }


//        for (int i = 0; i < row.size(); ) {
//            if (row.get(i) == 10) {
//                frames.add(new Strike());
//                i++;
//            } else if (row.get(i) + row.get(i + 1) == 10) {
//                frames.add(new Spare());
//                i+=2;
//            } else {
//                frames.add(new RegularFrame(row.get(i) + row.get(i + 1)));
//                i+=2;
//            }
//        }
        for (int i = 0; i < frames.size() -1; i++) {
            frames.get(i).setNext(frames.get(i+1));
        }

        return frames.stream().mapToInt(Frame::score).sum();


//        int i = 0;
//        while (i < row.size()) {
//            if (row.get(i) == 10) {
//                //strike
//                if (i + 1 < row.size()) {
//                    sum += row.get(i + 1) + row.get(i + 2);
//                }
//                i++;
//            } else if (row.get(i) + row.get(i + 1) == 10) {
//                //spare
//                if (i + 2 < row.size()) {
//                    sum += row.get(i + 2);
//                }
//                i += 2;
//            } else {
//                //nothing
//                i += 2;
//            }
//        }
//        return sum;
    }
}
