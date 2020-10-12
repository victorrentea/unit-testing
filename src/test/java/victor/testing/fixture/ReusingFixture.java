package victor.testing.fixture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


abstract class AbstractTestBase {
   protected long sysUserId;
   @BeforeEach
   public void initialize() {
      System.out.println("Common init");
      sysUserId = 1L; // pretend ID assigned at .persist time
   }
}

class MyTest extends AbstractTestBase{
   // TODO add a before here too
   @Test
   public void test() {
       // check sysUserId
   }
}