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
  private final URL baseUrl;

  public record SafetyResponse(String category, String detailsUrl) {
  }

  public boolean isSafe(String sku) {
    SafetyResponse response = restTemplate.getForEntity(
            baseUrl + "/product/{sku}/safety",
            SafetyResponse.class, sku)
        .getBody();
    return "SAFE".equals(response.category());
  }
}
