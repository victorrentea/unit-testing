package victor.testing.fixture;

import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractTestBase {
   protected long sysUserId;

   @BeforeEach
   public void initialize() {
      System.out.println("Common init");
      sysUserId = 1L; // pretend ID assigned at .persist time
   }
}
