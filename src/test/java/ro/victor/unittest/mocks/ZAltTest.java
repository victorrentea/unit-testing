package ro.victor.unittest.mocks;

import org.junit.Assert;
import org.junit.Test;

public class ZAltTest {
    @Test
    public void m2() {
        String s = new CodDeProd().m();
        Assert.assertEquals("ceva:8080", s);
    }
}