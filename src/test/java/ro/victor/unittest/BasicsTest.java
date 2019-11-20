package ro.victor.unittest;

import org.junit.Assert;
import org.junit.Test;

public class BasicsTest {

    int x = 1;

    public BasicsTest() {
        System.out.println("Uaaaa Uaaa");
    }

    @Test
    public void t1() {
        x ++;
        Assert.assertEquals(2,x);
    }
    @Test
    public void t2() {
        x ++;
        Assert.assertEquals(2,x);
    }
}
