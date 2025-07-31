package victor.testing.spring.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import victor.testing.spring.entity.ProductCategory;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@With
@Builder
public record ProductDto(
		Long id,
		@NotBlank String name,
		@NotNull String barcode,
		String supplierCode,
		ProductCategory category2,
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate createdDate) {
}