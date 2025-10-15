package victor.testing.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;

@ActiveProfiles("altu")
public class AltSpringContextTest extends IntegrationTest {

  @Test
  void createThrowsForUnsafeProduct() {
    // dummy
  }

}
