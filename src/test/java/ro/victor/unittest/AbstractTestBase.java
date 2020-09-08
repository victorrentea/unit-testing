package ro.victor.unittest;

import org.junit.Before;

public abstract class AbstractTestBase {
   @Before
   public final void initialize() {
      System.out.println("Cucu");
   }
}
