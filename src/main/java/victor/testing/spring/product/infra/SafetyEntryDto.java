package victor.testing.spring.product.infra;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SafetyEntryDto {
   private String category;
   private String detailsUrl;
}
