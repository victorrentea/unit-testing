package ro.victor.unittest;

import org.junit.Test;

import java.util.Random;

public class SimpleSimpleSimple {

    private String customerName = "Cust" + new Random().nextInt(10);

    public SimpleSimpleSimple() {
        System.out.println("Uaaa, Uaaa!@!");
    }

    @Test
    public void simple1() {
        System.out.println(customerName);

    }
    @Test
    public void simple2() {
        System.out.println(customerName);

    }
    @Test
    public void simple3() {
        System.out.println(customerName);

    }
}
