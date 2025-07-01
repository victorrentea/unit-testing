package victor.testing.spring.async;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@ConditionalOnProperty(name = "async.enabled", havingValue = "true", matchIfMissing = true)
public class AsyncConfig {
}
