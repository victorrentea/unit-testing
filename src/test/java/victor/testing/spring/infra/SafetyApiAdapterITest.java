package victor.testing.spring.infra;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import victor.testing.spring.IntegrationTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.matching.UrlPattern.ANY;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SafetyApiAdapterITest extends IntegrationTest {

  @Autowired
  SafetyApiAdapter adapter;

  @Autowired
  WireMockServer wireMock;

  @Test
  void safe() {
    wireMock.stubFor(get(ANY)
        .willReturn(okJson("""
            {"category": "SAFE"}
            """
        )));

    boolean result = adapter.isSafe("xyz");

    assertTrue(result);
  }

  @Test
  void unsafe() {
    wireMock.stubFor(get("/product/xyz/safety")
        .willReturn(okJson("""
            {"category": "NOT SAFE"}
            """
        )));

    boolean result = adapter.isSafe("xyz");

    assertFalse(result);
  }
}