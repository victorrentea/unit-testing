package victor.testing.spring.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProductDto {
	public Long id;
	@NotNull
//	@Size(min = 5)
	public String name;
	@NotNull
	public String barcode;
//	@NotEmpty
	public String supplierCode;
	public ProductCategory category;
//	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "MM/dd/yyyy")
	public LocalDate createdOn;

	public ProductDto(Product product) {
		name = product.getName();
		barcode = product.getBarcode();
		supplierCode = product.getSupplier().getCode();
		category = product.getCategory();
		createdOn = product.getCreatedDate();
	}

	public ProductDto(String name, String barcode, String supplierCode, ProductCategory category) {
		this.name = name;
		this.barcode = barcode;
		this.supplierCode = supplierCode;
		this.category = category;
	}
}
