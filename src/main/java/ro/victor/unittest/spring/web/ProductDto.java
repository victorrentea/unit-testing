package ro.victor.unittest.spring.web;

import ro.victor.unittest.spring.domain.Product;

public class ProductDto {
	public String productName;
	public String sampleDate;

	public ProductDto() {}
	public ProductDto(Product product) {
		this(product.getName());
	}

	public ProductDto(String productName) {
		this.productName = productName;
	}
}
