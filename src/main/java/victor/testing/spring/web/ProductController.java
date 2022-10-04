package victor.testing.spring.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;
import victor.testing.spring.web.dto.ProductDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

   private final ProductService facade;

   @PostMapping("product/create")
   public void create(@RequestBody ProductDto productDto) {
      facade.createProduct(productDto);
   }

//   @Secured("ADMIN")
//   @PreAuthorized("hasRole('ADMIN')")
   @PostMapping("product/search")
   public List<ProductSearchResult> search(@RequestBody @Validated ProductSearchCriteria criteria) {
      return facade.searchProduct(criteria);
   }


}
