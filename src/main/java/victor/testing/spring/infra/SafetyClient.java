package victor.testing.spring.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Slf4j
@Component
@RequiredArgsConstructor
public class SafetyClient {
//  private final RestTemplate restTemplate;
//  @Value("${safety.service.url.base}")
//  private final URL baseUrl;

  @Autowired
  SafetyApi safetyApi;

  public record SafetyResponse(String category, String detailsUrl) {
  }

  public boolean isSafe(String upc) {
//    SafetyResponse response = restTemplate.getForEntity(
//            baseUrl + "/product/{upc}/safety",
//            SafetyResponse.class, upc)
//        .getBody();
    SafetyResponse response  = safetyApi.getSafety(upc);
    return "SAFE".equals(response.category());
  }
}

@FeignClient(name="safety", url = "${safety.service.url.base}")
interface SafetyApi {
  @GetMapping("/product/{upc}/safety")
  SafetyClient.SafetyResponse getSafety(@PathVariable String upc);

}