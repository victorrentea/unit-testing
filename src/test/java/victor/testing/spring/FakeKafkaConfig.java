package victor.testing.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("in-mem-kafka")
public class FakeKafkaConfig {
}
