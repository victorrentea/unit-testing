package victor.testing.spring;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@EnableScheduling
@EnableCaching
@EnableAsync
@SpringBootApplication
@EnableJpaAuditing
public class TestedApplication {
  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .profiles("insertDummyData")
        .initializers(new WaitForDatabase())
        .sources(TestedApplication.class).run(args);
  }

  public static Optional<String> getUserOnCurrentThread() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Principal::getName);
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {
      builder.modules(new JavaTimeModule());
      builder.simpleDateFormat("yyyy-MM-dd");
      builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    };
  }

  @Autowired
  public void printDatabaseUrl(@Value("${spring.datasource.url}") String dbUrl) {
    log.info("Using database: {} <<<", dbUrl);
  }

  @Bean
  public AuditorAware<String> auditorProvider() {
    return TestedApplication::getUserOnCurrentThread;
  }

  @Bean
  public RestTemplate rest() {
    return new RestTemplate();
  }
}
