package victor.testing.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing // separate config for @WebMvcTest slice to work
@Configuration
public class JpaConfig {
}
