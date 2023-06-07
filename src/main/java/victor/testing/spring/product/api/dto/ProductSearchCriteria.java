package victor.testing.spring.product.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import victor.testing.spring.product.domain.ProductCategory;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductSearchCriteria { // smells like JSON
    public String name;
    public ProductCategory category;
    public Long supplierId;
}
