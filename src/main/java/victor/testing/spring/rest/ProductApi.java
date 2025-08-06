package victor.testing.spring.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.testing.spring.export.ProductExporter;
import victor.testing.spring.rest.dto.ProductDto;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;
import victor.testing.spring.service.GetProductService;
import victor.testing.spring.service.ProductService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductApi {
   private final ProductService service;
   private final ProductExporter productExporter;
   private final GetProductService getProductService;

   @PostMapping("product/create")
   @Secured("ROLE_ADMIN")
   public ResponseEntity<Void> create(@RequestBody @Validated ProductDto productDto) throws URISyntaxException {
      Long newProductId = service.createProduct(productDto);
      URI locationHeader = new URI("http://created-uri/" + newProductId);
      return ResponseEntity.created(locationHeader).build();
   }

   @PostMapping("product/search")
   public List<ProductSearchResult> search(@RequestBody ProductSearchCriteria criteria) {
      return service.searchProduct(criteria);
   }

   @GetMapping("product/{id}")
   public ProductDto get(@PathVariable long id) {
      return getProductService.getProduct(id);
   }

   @DeleteMapping("product/{id}")
   public void delete(@PathVariable long id) {
      service.deleteProduct(id);
   }

   @PostMapping("product/export")
   public String startExport() throws IOException {
      String fileName = productExporter.export();
      return "Exported to: " + fileName;
   }
}
