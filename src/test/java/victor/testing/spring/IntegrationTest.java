package victor.testing.spring;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.MonitorSpringStartupPerformance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.wiremock.spring.EnableWireMock;
import victor.testing.spring.service.ProductCreatedEvent;
import victor.testing.tools.AbstractTestListener;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@SpringBootTest // start the app in-memory
@ActiveProfiles("test") // use application-test.properties to override mai properties
@EmbeddedKafka // start an in-memory broker

@Import(IntegrationTest.TestKafkaListenersConfig.class) // test listeners
@EnableWireMock // starts an HTTP server on a random port to return JSON responses you pre-configure
@AutoConfigureMockMvc // MockMvc can send emulated HTTP requests without starting Tomcat
public class IntegrationTest {
  @Autowired
  protected MockMvc mockMvc; // emulates HTTP requests to your endpoints
  @Autowired
  protected AbstractTestListener<ProductCreatedEvent> productCreatedEventTestListener;

  @TestConfiguration
  public static class TestKafkaListenersConfig {
    @Bean
    public AbstractTestListener<ProductCreatedEvent> productCreatedEventTestListener() {
      return new ProductCreatedEventTestListener();
    }
  }

  public static class ProductCreatedEventTestListener extends AbstractTestListener<ProductCreatedEvent> {
    @KafkaListener(topics = PRODUCT_CREATED_TOPIC)
    public void receive(ConsumerRecord<String, ProductCreatedEvent> record) {
      super.receive(record);
    }
  }

  @AfterAll
  public static void checkHowManyTimesSpringStarted() {
    // PERFORMANCE DANGER: DO NOT CHANGE THIS CONSTANT!
    // CALL ME: 📞 0800ANARCHITECT (or you get fired :/)
    int ALLOWED_NUMBER_OF_TIMES_SPRING_STARTS = 2;
    assertThat(MonitorSpringStartupPerformance.startupTimeLogs)
        .describedAs("Number of times spring started (performance)")
        .hasSizeLessThanOrEqualTo(ALLOWED_NUMBER_OF_TIMES_SPRING_STARTS);
  }

}
