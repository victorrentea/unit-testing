package ro.victor.unittest.spring.facade;

import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Product.Category;

public class ProductSearchCriteria { // smells like JSON
    public String name;
    public Category category;
    public Long supplierId;
}
