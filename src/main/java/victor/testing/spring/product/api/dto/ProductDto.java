package victor.testing.spring.product.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.ProductCategory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProductDto {
	public Long id;
	@NotNull
	public String name;
	@NotNull
	public String sku;
	public Long supplierId;
	public ProductCategory category;
	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate createDate;

	public ProductDto(Product product) {
		name = product.getName();
		sku = product.getSku();
		supplierId = product.getSupplier().getId();
		category = product.getCategory();
		createDate = product.getCreateDate();
	}

	public ProductDto(String name, String sku, Long supplierId, ProductCategory category) {
		this.name = name;
		this.sku = sku;
		this.supplierId = supplierId;
		this.category = category;
	}
}
