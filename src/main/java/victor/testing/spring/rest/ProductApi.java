package victor.testing.spring.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;
import victor.testing.spring.rest.dto.ProductDto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductApi {
   private final ProductService service;

   @PostMapping("product/create")
   @Secured("ROLE_ADMIN")
   public ResponseEntity<Void> create(@RequestBody @Validated ProductDto productDto) throws URISyntaxException {
      Long id = service.createProduct(productDto);
      URI locationHeader = new URI("http://created-uri/" + id);
      return ResponseEntity.created(locationHeader).build();
   }

   @PostMapping("product/search")
   public List<ProductSearchResult> search(@RequestBody ProductSearchCriteria criteria) {
      return service.searchProduct(criteria);
   }

   @GetMapping("product/{id}")
   public ProductDto get(@PathVariable long id) {
      return service.getProduct(id);
   }

}