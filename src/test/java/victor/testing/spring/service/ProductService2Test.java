package victor.testing.spring.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.infra.SafetyClient;

@Transactional
public class ProductService2Test extends BaseTest {
//  @MockBean // replaces a spring bean with a Mockito mock reset between each test and injects that here for you
//  public SafetyClient mockSafetyClient;
  @Test
  void explore() {
    System.out.println("Me too");
  }
}
