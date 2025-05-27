package victor.testing.spring.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.testing.spring.batch.BatchExporter;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;
import victor.testing.spring.service.ProductService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductApi {
   private final ProductService service;
   private final BatchExporter batchExporter;

   @PostMapping("product/create")
   @Secured("ROLE_ADMIN")
   public ResponseEntity<Void> create(@RequestBody @Validated ProductDto productDto) throws URISyntaxException {
      log.info("Got: {}", productDto);
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

   @DeleteMapping("product/{id}")
   public void delete(@PathVariable long id) {
      service.deleteProduct(id);
   }

   @PostMapping("product/export")
   public String startExport() throws IOException {
      String fileName = batchExporter.export();
      return "Exported to: " + fileName;
   }
}
