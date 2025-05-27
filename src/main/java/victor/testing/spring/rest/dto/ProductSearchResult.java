package victor.testing.spring.rest.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Builder
public record ProductSearchResult(
    Long id,
    String name) {
}
