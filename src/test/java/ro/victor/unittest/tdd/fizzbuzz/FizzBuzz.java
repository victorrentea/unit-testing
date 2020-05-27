package ro.victor.unittest.tdd.fizzbuzz;

public class FizzBuzz {
    public String getItem(int i) {
        if (i % 15 == 0) {
            return "Fizz Buzz";
        }
        if (i % 3 == 0) {
            return "Fizz";
        }
        if (i % 5 == 0) {
            return "Buzz";
        }
        return i + "";
    }
}
