package victor.testing.spring.rest.dto;

import lombok.*;
import victor.testing.spring.entity.ProductCategory;

import java.util.Objects;

@Data
public final class ProductSearchCriteria {
  private String name;
  private ProductCategory category;
  private Long supplierId;
}
