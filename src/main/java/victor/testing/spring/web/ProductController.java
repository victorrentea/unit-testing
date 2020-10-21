package victor.testing.spring.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
//@PreAuthorize("hasRole('ADMIN')")
public class ProductController {

   private final ProductService facade;

   @PostMapping("product/create")
   public Long create(@RequestBody ProductDto productDto) {
      return facade.createProduct(productDto);
   }

   @PostMapping("product/search")
   public List<ProductSearchResult> search(@RequestBody ProductSearchCriteria criteria) {
      if (criteria.name.equals("A")) {
         throw new IllegalStateException();
      }
      return facade.searchProduct(criteria);
   }


}
