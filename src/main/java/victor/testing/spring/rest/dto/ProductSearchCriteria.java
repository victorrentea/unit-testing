package victor.testing.spring.rest.dto;

import lombok.*;
import victor.testing.spring.entity.ProductCategory;

@Builder
@With
public record ProductSearchCriteria( // received as JSON from client
    String name,
    ProductCategory category,
    Long supplierId
) {
    public static ProductSearchCriteria empty() {
        return ProductSearchCriteria.builder().build();
    }
}
