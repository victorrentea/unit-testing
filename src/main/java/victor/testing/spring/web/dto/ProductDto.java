package victor.testing.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;

@Data
@AllArgsConstructor
public class ProductDto {
	public String name;
	public String upc;
	public Long supplierId;
	public ProductCategory category;
}
