package victor.testing.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	@NotNull
	public String name;
	public String barcode;
	public Long supplierId;
	public ProductCategory category;

	public ProductDto(Product product) {
		name = product.getName();
		barcode = product.getBarcode();
		supplierId = product.getSupplier().getId();
		category = product.getCategory();
	}
}
