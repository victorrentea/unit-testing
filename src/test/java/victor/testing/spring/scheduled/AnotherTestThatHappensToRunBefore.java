package victor.testing.spring.scheduled;

import org.junit.jupiter.api.Test;
import victor.testing.spring.BaseDatabaseTest;

// fix
// @TestPropertySource(properties = "email.sender.cron=-")
public class AnotherTestThatHappensToRunBefore extends BaseDatabaseTest {
  @Test
  void justAnotherTest() {
  }

}
