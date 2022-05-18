package victor.testing.tdd.bowling;

public class Game {

    // TODO array.
    private int total;

    public void roll(int pins) {
        total += pins;
    }

    public int score() {
        return total;
    }

}