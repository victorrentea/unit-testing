package victor.testing.spring.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProductDto {
	public Long id;
	@NotNull
	public String name;
	public String barcode;
	public Long supplierId;
	public ProductCategory category;
	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate createDate;

	public ProductDto(Product product) {
		name = product.getName();
		barcode = product.getBarcode();
		supplierId = product.getSupplier().getId();
		category = product.getCategory();
		createDate = product.getCreateDate();
	}

	public ProductDto(String name, String barcode, Long supplierId, ProductCategory category) {
		this.name = name;
		this.barcode = barcode;
		this.supplierId = supplierId;
		this.category = category;
	}
}
