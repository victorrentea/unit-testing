package victor.testing.spring;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

@Slf4j
@EnableCaching
@SpringBootApplication
public class SomeSpringApplication{
    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .profiles("insertDummyData")
            .initializers(new WaitForDatabase())
            .sources(SomeSpringApplication.class).run(args);
    }


//    @TestConfiguration // il pui in clasa de test.
//    public static class AsyncDisable implements AsyncConfigurer  {
//        // din teste, faci asa
//        @Override
//        public Executor getAsyncExecutor() {
//            // @Async da submit acestui executor care ruleaza in threadul original. fara async.
//            return new Executor() {
//                @Override
//                public void execute(@NotNull Runnable command) {
//                    command.run();
//                }
//            };
//        }
//    }

    @Autowired
    public void printDatabaseUrl(@Value("${spring.datasource.url}") String dbUrl) {
        log.info("Using database: {} <<<", dbUrl);
    }

    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }
}
