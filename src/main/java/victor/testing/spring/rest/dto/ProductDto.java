package victor.testing.spring.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProductDto {
	public Long id;
	@NotNull
	public String name;
	@NotNull
	public String barcode;
	public String supplierCode;
	public ProductCategory category;
	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate createdDate;

	public ProductDto(Product product) {
		name = product.getName();
		barcode = product.getBarcode();
		supplierCode = product.getSupplier().getCode();
		category = product.getCategory();
		createdDate = product.getCreatedDate();
	}

	public ProductDto(String name, String barcode, String supplierCode, ProductCategory category) {
		this.name = name;
		this.barcode = barcode;
		this.supplierCode = supplierCode;
		this.category = category;
	}
}
