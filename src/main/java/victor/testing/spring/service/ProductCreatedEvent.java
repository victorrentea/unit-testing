package victor.testing.spring.service;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ProductCreatedEvent(
    Long productId,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

    LocalDateTime observedAt
) {
}
