package victor.testing.spring.service;

import java.time.LocalDateTime;

public record ProductCreatedEvent(
    Long productId,
    LocalDateTime observedAt
) {
}
