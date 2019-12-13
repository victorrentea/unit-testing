package ro.victor.unittest.tdd.bowling;

import java.util.List;
//interface Frame {
//    int score();
//}
//class RegularFrame implements Frame {
//    private final int first;
//    private final int second;
//    RegularFrame(int first, int second) {
//        this.first = first;
//        this.second = second;
//    }
//    @Override
//    public int score() {
//        return first+second;
//    }
//}
//class Spare implements Frame {
//    @Override
//    public int score() {
//        return 10;
//    }
//}instanceof


public class BowlingGame {
    public static int calculateScore(List<Integer> row) {
        int sum = row.stream().mapToInt(i->i).sum();
        int i = 0;
        while (i < row.size()) {
            if (row.get(i) == 10) {
                //strike
                if (i + 1 < row.size()) {
                    sum += row.get(i + 1) + row.get(i + 2);
                }
                i ++;
            } else if (row.get(i) + row.get(i + 1) == 10) {
                //spare
                if (i + 2 < row.size()) {
                    sum += row.get(i + 2);
                }
                i += 2;
            } else {
                //nothing
                i += 2;
            }
        }
        return sum;
    }
}
