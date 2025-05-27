package victor.testing.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Import(ProductCreatedEventTestListener.class)
@ActiveProfiles("testcontainers-playtika")
@AutoConfigureMockMvc
public class IntegrationTest {
  protected final static ObjectMapper json = new ObjectMapper().registerModule(new JavaTimeModule());

  @Autowired
  protected MockMvc mockMvc;

  /* ==== Connect local DSL with remote WireMock running in the Testcontainer ==== */
  @Value("${embedded.wiremock.host}")
  String wiremockHost;

  @Value("${embedded.wiremock.port}")
  int wiremockPort;

  @BeforeEach
  final void configureWireMockDSL() {
    WireMock.configureFor(wiremockHost, wiremockPort);
  }

  @AfterEach
  public void resetWireMock() {
    WireMock.resetAllRequests();
  }

  @Autowired
  protected ProductCreatedEventTestListener testListener;

}
