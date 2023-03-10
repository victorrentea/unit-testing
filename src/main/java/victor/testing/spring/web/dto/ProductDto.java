package victor.testing.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ProductDto {
	@NotNull
	public String name;
	public String barcode;
	public Long supplierId;
	public ProductCategory category;
}
