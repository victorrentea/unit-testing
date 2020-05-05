package ro.victor.unittest.spring.facade;

import ro.victor.unittest.spring.domain.Product;

public class ProductSearchCriteria { // smells like JSON
    public String name;
    public Product.Category category;
}
