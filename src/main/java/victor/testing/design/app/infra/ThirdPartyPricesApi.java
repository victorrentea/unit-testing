package victor.testing.design.app.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("price-service")
public interface ThirdPartyPricesApi {
   @GetMapping("/product/{id}/price")
   double fetchPrice(@PathVariable Long id);
}
