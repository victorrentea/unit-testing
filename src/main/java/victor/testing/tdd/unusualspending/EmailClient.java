package victor.testing.tdd.unusualspending;

import reactor.core.publisher.Mono;

public interface EmailClient {
    void sendEmail(String userId, String subject, String body);
}
