package victor.testing.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	public String name;
	public String upc;
	public Long supplierId;
	public ProductCategory category;
}
