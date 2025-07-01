package victor.testing.spring;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.MonitorSpringStartupPerformance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import victor.testing.spring.message.SomeServiceWithAllTheLogic;

import java.io.IOException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
//@Testcontainers DON'T! => // https://testcontainers.com/guides/testcontainers-container-lifecycle/
@SpringBootTest
@Import({ProductCreatedEventTestListener.class, OutQueueTestListener.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IntegrationTest {
  @Autowired
  protected ProductCreatedEventTestListener testListener;

  @Autowired
  protected OutQueueTestListener outQueueTestListener;

  @MockitoSpyBean // @SpyBean = "wrap" the real bun with a mockito spy so that I can intercept its method calls
  protected SomeServiceWithAllTheLogic service;

  @Container
  static LocalStackContainer localStack = new LocalStackContainer(
      DockerImageName.parse("localstack/localstack:3.4.0"))
      .withServices(LocalStackContainer.Service.SQS);

  @Container
  static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:2.35.0");

  static PostgreSQLContainer postgres = new PostgreSQLContainer(
      DockerImageName.parse("postgres:16-alpine"))
      .withDatabaseName("test")
      ;

  static {
    localStack.start();
    wiremockServer.start();
    postgres.start();
  }

  private static void createQueue(String queueName) {
    try {
      localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", queueName);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }


  @DynamicPropertySource
  static void injectPropertiesToSpring(DynamicPropertyRegistry registry) {
    registry.add("wiremock.host", () -> wiremockServer.getHost());
    registry.add("wiremock.port", () -> wiremockServer.getPort());

    registry.add("spring.cloud.aws.sqs.endpoint", () -> localStack.getEndpointOverride(LocalStackContainer.Service.SQS).toString());
    registry.add("spring.cloud.aws.credentials.access-key", () -> localStack.getAccessKey());
    registry.add("spring.cloud.aws.credentials.secret-key", () -> localStack.getSecretKey());
    registry.add("spring.cloud.aws.region.static", () -> localStack.getRegion());

    registry.add("spring.datasource.url", postgres::getJdbcUrl); // random port
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);

    var supplierCreatedQueueName = "supplier-created-event-" + new Random().nextInt(99999);
    createQueue(supplierCreatedQueueName);
    registry.add("supplier.created.event", () -> supplierCreatedQueueName);

    var productCreatedQueueName = "product-created-topic-" + new Random().nextInt(99999);
    createQueue(productCreatedQueueName);
    registry.add("product.created.topic", () -> productCreatedQueueName);
  }

  @Value("${wiremock.host}")
  String wiremockHost;
  @Value("${wiremock.port}")
  int wiremockPort;
  @BeforeEach
  final void configureWireMockDSL() {
    WireMock.configureFor(wiremockHost, wiremockPort);
  }
  @AfterEach
  public void resetWireMock() {
    WireMock.resetAllRequests();
  }




  @AfterAll
  public static void checkHowManyTimesSpringStarted() {
    // PERFORMANCE DANGER: DO NOT CHANGE THIS CONSTANT!
    // CALL ME: 📞 0800ANARCHITECT (or you get fired :/)
    int ALLOWED_NUMBER_OF_TIMES_SPRING_STARTS = 7;
    assertThat(MonitorSpringStartupPerformance.startupTimeLogs)
        .describedAs("Number of times spring started (performance)")
        .hasSizeLessThanOrEqualTo(ALLOWED_NUMBER_OF_TIMES_SPRING_STARTS);
  }


}
