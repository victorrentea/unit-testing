package victor.testing.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@SpringBootApplication
public class SomeSpringApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SomeSpringApplication.class).run(args);
    }


}
