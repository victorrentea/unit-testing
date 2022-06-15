package victor.testing.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Slf4j
@EnableCaching
@SpringBootApplication
public class SomeSpringApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .profiles("insertDummyData")
            .initializers(new WaitForDatabase())
            .sources(SomeSpringApplication.class).run(args);
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
