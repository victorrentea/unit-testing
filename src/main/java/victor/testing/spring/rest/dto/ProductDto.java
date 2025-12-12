package victor.testing.spring.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@With
@Builder
public record ProductDto(
		Long id,
    @Size(min = 4)
		@NotBlank
    String name,
		@NotNull String barcode,
		String supplierCode,
		ProductCategory category,
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate createdDate) {
}