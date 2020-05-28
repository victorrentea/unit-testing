package ro.victor.unittest.tdd.fizzbuzz;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FizzBuzzTest {
    // Se cere o clasa ----- care sa ia prin constructor nr N
    // si apoi printr-o metoda getItem(i):String sa iti dea stringul respectiv

    private final FizzBuzz fizzBuzz = new FizzBuzz();


    @Test
    public void test1() {
        assertEquals("1", fizzBuzz.getItem(1));
    }

    @Test // desi duplicat, il las ca sa demonstreze doua puncte pe acea axa
    public void test2() {
        assertEquals("2", fizzBuzz.getItem(2));
    }

    @Test
    public void test3() {
        assertEquals("Fizz", fizzBuzz.getItem(3));
    }
//    @Test // duplicate test care nu aduce nici o alta informatie
//    public void test4() {
//        assertEquals("4", fizzBuzz.getItem(4));
//    }
    @Test
    public void test5() {
        assertEquals("Buzz", fizzBuzz.getItem(5));
    }
    @Test
    public void test6() {
        assertEquals("Fizz", fizzBuzz.getItem(6));
    }
    @Test
    public void test10() {
        assertEquals("Buzz", fizzBuzz.getItem(10));
    }
    @Test
    public void test15() {
        assertEquals("Fizz Buzz", fizzBuzz.getItem(15));
    }
    @Test
    public void test105() {
        assertEquals("Fizz Buzz Wizz", fizzBuzz.getItem(3*5*7));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testNegative() {
        fizzBuzz.getItem(1);
    }

    // 7 Wizz
    // 21 Fizz Wizz
    // 105 Fizz Buzz Wizz

    // Kent Beck: You have to do a difficult change. \
    // First you make that change easy [Warning: this might be difficult] -REFACTOR
    // Then you do the ease change
}
