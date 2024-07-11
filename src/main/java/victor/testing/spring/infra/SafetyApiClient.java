package victor.testing.spring.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Slf4j
@Component
@RequiredArgsConstructor
public class SafetyApiClient {
  private final SafetyFeignClient safetyFeignClient; // TODO use

  private final RestTemplate restTemplate;
  @Value("${safety.service.url.base}")
  private final URL baseUrl;

  public record SafetyResponse(
      String category,
//      String categoryBUG,
      String detailsUrl) {
  }

//  @Retryable
  public boolean isSafe(String barcode) {
    // tokens to send
//    SafetyResponse response = restTemplate.getForEntity(
////            baseUrl + "/product/{barcode}/safBUGety",
//            baseUrl + "/product/{barcode}/safety",
//            SafetyResponse.class, barcode)
//        .getBody();
    SafetyResponse response = safetyFeignClient.getSafety(barcode);
//    return "sAFEBUG".equals(response.category());
    return "SAFE".equals(response.category());
  }
}
