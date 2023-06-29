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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;

@Slf4j
@EnableScheduling
@EnableCaching
@EnableAsync
@SpringBootApplication
public class TestedApplication {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.modules(new JavaTimeModule());
            builder.simpleDateFormat("yyyy-MM-dd");
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        };
    }
    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .profiles("insertDummyData")
            .initializers(new WaitForDatabase())
            .sources(TestedApplication.class).run(args);
    }

    @Autowired
    public void printDatabaseUrl(@Value("${spring.datasource.url}") String dbUrl) {
        log.info("Using database: {} <<<", dbUrl);
    }

    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }
}
