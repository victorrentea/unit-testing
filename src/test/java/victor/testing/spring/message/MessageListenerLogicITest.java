package victor.testing.spring.message;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

import static org.assertj.core.api.Assertions.assertThat;

class MessageListenerLogicITest extends IntegrationTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  MessageListener messageListener;
  @Autowired
  private ProductRepo productRepo;

  @BeforeEach
  @AfterEach
  final void cleanDB() { // manual cleanup required as tested code COMMITs an INSERT
    productRepo.deleteAll();
    supplierRepo.deleteAll();
  }

  @Test
  void listenerLogic() {
    // direct call in-thread. no async/kafka involved
    messageListener.onMessage("supplier");

    assertThat(supplierRepo.findByName("supplier")).isNotNull();
  }

}
