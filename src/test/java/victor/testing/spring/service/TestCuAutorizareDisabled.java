package victor.testing.spring.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest//(exclude = { SecurityAutoConfiguration.class })
@EmbeddedKafka
@ActiveProfiles("test")
public class TestCuAutorizareDisabled {
  @Autowired
  private ProductService productService;
  @Test
  @Disabled
  void test() {
    productService.cevaLogica();
  }

  @TestConfiguration
  @EnableMethodSecurity(securedEnabled = false, prePostEnabled = true)
  static class FaraConfigTeRog {

  }
}
