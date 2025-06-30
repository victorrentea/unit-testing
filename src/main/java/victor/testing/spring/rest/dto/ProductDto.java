package victor.testing.spring.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@With
@Builder
public record ProductDto(
		Long id,
		@NotNull String name,
		@NotNull String barcode,
		String supplierCode,
		ProductCategory category,
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate createdDate) {
}