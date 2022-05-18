package victor.testing.tdd.unusualspending;

import reactor.core.publisher.Mono;

public interface EmailClient {
    Mono<Void> sendEmail(String userId, String subject, String body);
}
