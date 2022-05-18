package victor.testing.tdd.bowling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {

    private final List<Integer> rolls = new ArrayList<>();

    public void roll(int pins) {
        rolls.add(pins);
    }

    public int score() {
        int bonusForSpares = 0;
        for (int i = 0; i < rolls.size(); i+=2) {
            int pinsInFrame = rolls.get(i)  + rolls.get(i+1);
            if (pinsInFrame == 10) {
                bonusForSpares += rolls.get(i + 2);
            }
        }
        return bonusForSpares + rolls.stream().mapToInt(i->i).sum();
    }
}

// if Frame were part of the API, then make it immutable.
//record Frame(int roll1, Optional<Integer> roll2, Optional<Integer> roll3){}

//class Frame{
//    private final int roll1;
//    private Integer roll2;
//
//    Frame(int roll1) {
//        this.roll1 = roll1;
//    }
//
//    public void setRoll2(Integer roll2) {
//        this.roll2 = roll2;
//    }
//
//}