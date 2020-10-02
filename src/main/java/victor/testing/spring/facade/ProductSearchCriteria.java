package victor.testing.spring.facade;

import victor.testing.spring.domain.Product.Category;

public class ProductSearchCriteria { // smells like JSON
    public String name;
    public Category category;
    public Long supplierId;
}
