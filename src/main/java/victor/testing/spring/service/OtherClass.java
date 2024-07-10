package victor.testing.spring.service;

import victor.testing.spring.api.dto.ProductDto;

public class OtherClass {
  private final ProductService productService;

  public OtherClass(ProductService productService) {
    this.productService = productService;
  }

  public void me() {
    productService.newProduct(new ProductDto());
    // offend all the static code analysis tools (SonarLint, IntelliJ inspection, FindBugs)
  // any other class in src/main from using this method.
  }
}
