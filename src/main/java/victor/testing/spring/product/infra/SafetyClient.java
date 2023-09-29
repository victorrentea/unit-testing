package victor.testing.spring.product.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Slf4j
@Component
@RequiredArgsConstructor
public class SafetyClient {
  private final RestTemplate restTemplate;
  @Value("${safety.service.url.base}")
  private URL baseUrl;

  public boolean isSafe(String sku) {
    SafetyReportDto response = restTemplate.getForEntity(
            baseUrl + "/product/{sku}/safety",
            SafetyReportDto.class, sku)
        .getBody();

    // to avoid bugs here, IBNCLUDE THIS In our tests
    // somy parsing logic of their response body
    boolean safe = response.getEntries().stream()
        .anyMatch(report -> "SAFE".equals(report.getCategory()));
    log.info("Product is safe: " + safe);
    return safe;
  }

}
