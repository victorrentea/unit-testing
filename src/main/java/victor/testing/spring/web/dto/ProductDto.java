package victor.testing.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ProductDto {
	public String name;
	public String barcode;
	@NotNull
	public Long supplierId;
	public ProductCategory category;
}
