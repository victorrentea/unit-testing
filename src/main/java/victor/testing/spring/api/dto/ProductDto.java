package victor.testing.spring.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProductDto {
	public Long id;
	@NotNull
	public String name;
	@NotNull
	public String upc;
	public String supplierCode;
	public ProductCategory category;
	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate createdDate;

	public ProductDto(Product product) {
		name = product.getName();
		upc = product.getUpc();
		supplierCode = product.getSupplier().getCode();
		category = product.getCategory();
		createdDate = product.getCreatedDate();
	}

	public ProductDto(String name, String upc, String supplierCode, ProductCategory category) {
		this.name = name;
		this.upc = upc;
		this.supplierCode = supplierCode;
		this.category = category;
	}
}
