package victor.testing.spring.infra;

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
  private final SafetyFeignClient safetyFeignClient; // TODO use

  private final RestTemplate restTemplate;
  @Value("${safety.service.url.base}")
  private final URL baseUrl;

  public record SafetyResponse(String category, String detailsUrl) {
  }

  // asta e un ADapter pattern ( o clasa care intermediaza accesul la apiuri externe)
  public boolean isSafe(String upc) {
    SafetyResponse response = restTemplate.getForEntity(
            baseUrl + "/product/{upc}/safety",
            SafetyResponse.class, upc)
        .getBody();
    return "SAFE".equals(response.category());
  }
  // a) si b) pot fi evitate daca iti generezi clientul de HTTP
  // din OpenAPI/swagger expus de serviciul extern
}
