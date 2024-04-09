package victor.testing.spring.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.api.dto.ProductSearchCriteria;
import victor.testing.spring.api.dto.ProductSearchResult;
import victor.testing.spring.api.dto.ProductDto;

import javax.validation.Valid;
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
   public ResponseEntity<Void> create(@RequestBody @Valid ProductDto productDto) throws URISyntaxException {
      service.createProduct(productDto);
      return ResponseEntity.created(new URI("http://created-uri")).build();
   }

   @PostMapping("product/search")
   public List<ProductSearchResult> search(@RequestBody ProductSearchCriteria criteria) {
      return service.searchProduct(criteria);
   }

   @GetMapping("product/{id}")
   public ProductDto get(@PathVariable long id) {
      return service.getProduct(id);
   }
//   @GetMapping("product/ceva") // ceva?id=1234
//   public ProductDto getCuParam(@RequestBody Obiect id, @RequestParam MultipartFile file) {
//      return service.getProduct(id);
//   }

}
