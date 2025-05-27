package victor.testing.spring.rest.dto;

import lombok.Builder;
import lombok.With;
import victor.testing.spring.entity.ProductCategory;

@With
@Builder
public record ProductSearchCriteria(
    String name,
    ProductCategory category,
    Long supplierId) {

  public static ProductSearchCriteria empty() {
    return ProductSearchCriteria.builder().build();
  }
}
