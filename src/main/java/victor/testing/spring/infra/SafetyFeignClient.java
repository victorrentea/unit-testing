package victor.testing.spring.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="safety", url = "${safety.service.url.base}")
interface SafetyFeignClient {
  @GetMapping("/product/{barcode}/safety")
  SafetyApiClient.SafetyResponse getSafety(@PathVariable String barcode);

}