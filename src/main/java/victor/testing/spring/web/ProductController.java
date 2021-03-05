package victor.testing.spring.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;
import victor.testing.spring.web.dto.ProductDto;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

   private final ProductService facade;

   @PostMapping("product/create")
   public Long create(@RequestBody ProductDto productDto) {
      return facade.createProduct(productDto);
   }

//   @PreAuthorize("hasRole('ADMIN')")
   @PostMapping("product/search")
   public List<ProductSearchResult> search(@RequestBody ProductSearchCriteria criteria/*,
                                           @AuthenticationPrincipal Authentication principal*/) {
      System.out.println("Current user:" + 		SecurityContextHolder.getContext().getAuthentication().getName());
//      System.out.println("Current user : " + principal.getName());
      return facade.searchProduct(criteria);
   }


}
