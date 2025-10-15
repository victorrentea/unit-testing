package victor.testing.spring;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterAll;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.MonitorSpringStartupPerformance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.wiremock.spring.EnableWireMock;
import victor.testing.spring.service.ProductCreatedEvent;
import victor.testing.tools.AbstractTestListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@SpringBootTest // start the app in-memory
@ActiveProfiles("test") // use application-test.properties to override src/main properties

@EmbeddedKafka // start an in-memory broker
@Import(IntegrationTest.TestKafkaListenersConfig.class) // test listeners

@EnableWireMock // starts WireMock HTTP server on a random port ${wiremock.server.port} returning pre-configured JSON responses
@AutoConfigureMockMvc // MockMvc can send emulated HTTP requests without starting Tomcat
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class IntegrationTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected AbstractTestListener<ProductCreatedEvent> productCreatedEventTestListener;

  // ca sa reduci nr de context de spring boot-ate in test,
  // in loc de @MockBean in clasa ta, ridica-l ca @SpyBean in superclasa de test.
  @MockitoSpyBean
//  @SpyBean
//  @MockitoBean(answers = Answers.CALLS_REAL_METHODS)
  protected KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

  @TestConfiguration
  public static class TestKafkaListenersConfig {
    @Bean
    public AbstractTestListener<ProductCreatedEvent> productCreatedEventTestListener() {
      return new AbstractTestListener<>() {
        @KafkaListener(topics = PRODUCT_CREATED_TOPIC)
        public void receive(ConsumerRecord<String, ProductCreatedEvent> record) {
          super.receive(record);
        }
      };
    }
    @Bean
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate(ProducerFactory producerFactory) {
        return new KafkaTemplate(producerFactory);
    }
  }

  @AfterAll
  public static void checkHowManyTimesSpringStarted() {
    // PERFORMANCE DANGER: DO NOT INCREASE THIS CONSTANT! -> CALL ME: 📞 0800ANARCHITECT
    int ALLOWED_NUMBER_OF_TIMES_SPRING_STARTS = 2;
    assertThat(MonitorSpringStartupPerformance.startupTimeLogs)
        .describedAs("Number of times spring started (performance)")
        .hasSizeLessThanOrEqualTo(ALLOWED_NUMBER_OF_TIMES_SPRING_STARTS);
  }

}
