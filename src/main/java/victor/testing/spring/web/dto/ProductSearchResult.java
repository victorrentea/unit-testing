package victor.testing.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import victor.testing.spring.domain.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchResult {
    private Long id;
    private String name;


   public ProductSearchResult(Product product) {
      id = product.getId();
      name = product.getName();
   }
}
