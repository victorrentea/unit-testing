package victor.testing.spring.service;

import victor.testing.spring.web.dto.ProductDto;

public class ANotherService {
    private final ProductService productService;

    public ANotherService(ProductService productService) {
        this.productService = productService;
    }

    public void method() {
        productService.getProductOMG(null);
    }
}
