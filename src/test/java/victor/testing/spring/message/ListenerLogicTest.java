package victor.testing.spring.message;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.SupplierRepo;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
@TestPropertySource(properties = "kafka.enabled=true")
public class ListenerLogicTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  MessageListener messageListener;

  @BeforeEach
  @AfterEach
  final void cleanDB() { // manual cleanup required as tested code COMMITs an INSERT
    supplierRepo.deleteAll();
  }

  @Test
  void listenerLogic() {
    // direct call in-thread. no async/kafka involved
    messageListener.onMessage("supplier");

    assertThat(supplierRepo.findByName("supplier")).isNotNull();
  }

}
