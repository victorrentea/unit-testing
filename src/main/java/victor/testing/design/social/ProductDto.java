package victor.testing.design.social;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import victor.testing.design.app.domain.Product;
import victor.testing.design.app.domain.ProductCategory;

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
	public LocalDate createdDate;

}
