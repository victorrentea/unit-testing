package victor.testing.spring.rest.dto;

import lombok.*;

@Builder
@With
public record ProductSearchResult(
    Long id,
    String name) {
}
