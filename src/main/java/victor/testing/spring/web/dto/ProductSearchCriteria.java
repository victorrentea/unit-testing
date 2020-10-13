package victor.testing.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import victor.testing.spring.domain.ProductCategory;

@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCriteria { // smells like JSON
//    public Long id;
    public String name;  // name: [________]
    public ProductCategory category;// category: [________|v]
    public Long supplierId; // supplier: [________|v]
}
