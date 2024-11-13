package victor.testing.spring.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import victor.testing.spring.infra.SafetyApiAdapter.SafetyResponse;

import java.net.URL;

@Slf4j
@Component
@RequiredArgsConstructor
public class SafetyApiAdapter {
  private final SafetyApiFeignClient safetyApiFeignClient; // TODO use

  private final RestTemplate restTemplate;
  @Value("${safety.service.url.base}")
  private final URL baseUrl;

  public record SafetyResponse(String category, String detailsUrl) {
  }

  public boolean isSafe(String barcode) {
    // foarte ok de folosit rest template
    SafetyResponse response = restTemplate.getForEntity(
            baseUrl + "/product/{barcode}/safety",
            SafetyResponse.class, barcode)
        .getBody();

    // replacement pt RestTemplate (nu merita sa migrezi)
//    var response = webClient // welcome to Reactive HELL☠️

//    var response = RestClient.create() // nu-ti crea tu de mana ci foloseste o instanta injectata

//    var response = restClient
//        .get()
//        .uri(baseUrl + "/product/{barcode}/safety", barcode)
//        .retrieve()
//        .body(SafetyResponse.class);

//    var response = client.fetchSafety(barcode);

    return "SAFE".equals(response.category());
  }
//  private final ProductApiClient client;
}

//@FeignClient("product-client")
//interface ProductApiClient { // must-have pentru un eco de microservicii
//  @GetMapping("/product/{barcode}/safety")
//  SafetyResponse fetchSafety(@PathVariable String barcode);
//}
