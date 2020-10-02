package victor.testing.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import victor.testing.spring.domain.ProductCategory;

@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCriteria { // smells like JSON
    public String name;
    public ProductCategory category;
    public Long supplierId;
}
