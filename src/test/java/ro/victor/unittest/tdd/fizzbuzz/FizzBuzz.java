package ro.victor.unittest.tdd.fizzbuzz;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

public class FizzBuzz {
    public String getItem(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }
        List<String> tokens = new ArrayList<>();
        if (i % 3 == 0) {
           tokens.add("Fizz");
        }
        if (i % 5 == 0) {
           tokens.add("Buzz");
        }
        if (i % 7 == 0) {
           tokens.add("Wizz");
        }
        if (!tokens.isEmpty()) {
            return join(" ", tokens);
        }
        return i + "";
    }
}
