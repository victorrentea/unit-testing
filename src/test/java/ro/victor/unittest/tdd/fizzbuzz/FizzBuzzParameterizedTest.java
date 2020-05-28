package ro.victor.unittest.tdd.fizzbuzz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FizzBuzzParameterizedTest {
    // Se cere o clasa ----- care sa ia prin constructor nr N
    // si apoi printr-o metoda getItem(i):String sa iti dea stringul respectiv

    private final FizzBuzz fizzBuzz = new FizzBuzz();

    private final int input;
    private final String expectedOutput;

    public FizzBuzzParameterizedTest(int input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    // e mai bun decat a face un for peste colectia cu intrari/iesiri,
    // pentru ca vezi fiecare test rulat in izolare

    @Parameterized.Parameters(name = "{0} -> {1}")
    public static Object[][] getData() {
        return new Object[][]{
            {1, "1"},
            {2, "2"},
            {3, "Fizz"},
            {15, "Fizz Buzz"},
            {3 * 5 * 7, "Fizz Buzz Wizz"},
        };
    }

    @Test
    public void test() {
        assertEquals(expectedOutput, fizzBuzz.getItem(input));
    }
}
