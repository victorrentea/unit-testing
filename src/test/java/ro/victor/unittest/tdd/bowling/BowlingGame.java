package ro.victor.unittest.tdd.bowling;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BowlingGame {
    public static int calculateScore(int[] row) {
        int sum = IntStream.of(row).sum();
        int i = 0;
        while (i < row.length) {
            if (row[i] == 10) {
                //strike
                if (i + 1 < row.length) {
                    sum += row[i + 1] + row[i + 2];
                }
                i ++;
            } else if (row[i] + row[i + 1] == 10) {
                //spare
                if (i + 2 < row.length) {
                    sum += row[i + 2];
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
