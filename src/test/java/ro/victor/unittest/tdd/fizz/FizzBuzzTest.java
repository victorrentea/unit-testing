package ro.victor.unittest.tdd.fizz;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class FizzBuzzTest {

    private static final Map<Integer, String> FACTORS = new HashMap<>();
    static {
        FACTORS.put(3,"Fizz");
        FACTORS.put(5,"Buzz");
        FACTORS.put(7,"Wizz");
    }
    private FizzBuzz fizzBuzz = new FizzBuzz(FACTORS);

    @Test
    public void testNumber1() {
        Assert.assertEquals("1", fizzBuzz.getString(0+1));
    }

    @Test
    public void testNumber2() {
        Assert.assertEquals("2", fizzBuzz.getString(1+1));
    }

    @Test
    public void testNumber3() {
        Assert.assertEquals("Fizz", fizzBuzz.getString(2+1));
    }


    @Test
    public void testNumber5() {
        Assert.assertEquals("Buzz", fizzBuzz.getString(4+1));
    }

    @Test
    public void testNumber15() {
        Assert.assertEquals("FizzBuzz", fizzBuzz.getString(14+1));
    }

    @Test
    public void testNumber7() {
        Assert.assertEquals("Wizz", fizzBuzz.getString(6+1));
    }

    @Test
    public void testNumber21() {
        Assert.assertEquals("FizzWizz", fizzBuzz.getString(20+1));
    }
    @Test
    public void testNumber35() {
        Assert.assertEquals("BuzzWizz", fizzBuzz.getString(34+1));
    }

}
