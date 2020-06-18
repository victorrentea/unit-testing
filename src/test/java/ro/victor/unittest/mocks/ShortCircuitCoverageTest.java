package ro.victor.unittest.mocks;

import org.junit.Test;

public class ShortCircuitCoverageTest {
   @Test
   public void test() {
      new ShortCircuitCoverage().m(true);
      new ShortCircuitCoverage().m(false);

   }
}
