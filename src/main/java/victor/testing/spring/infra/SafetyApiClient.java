package victor.testing.spring.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URL;

@Slf4j
@Component
@RequiredArgsConstructor
public class SafetyApiClient {
  private final RestClient restClient;
  @Value("${safety.service.url.base}")
  private final URL baseUrl;

  public record SafetyResponse(String category, String detailsUrl) {
  }

  public boolean isSafe(String barcode) {
    SafetyResponse response = restClient.get()
            .uri(baseUrl + "/product/{barcode}/safety", barcode)
            .retrieve()
            .body(SafetyResponse.class);
    return "SAFE".equals(response.category());
  }
}
