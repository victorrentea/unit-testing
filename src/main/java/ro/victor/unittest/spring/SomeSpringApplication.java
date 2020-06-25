package ro.victor.unittest.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ro.victor.unittest.spring.web.CustomHeaderInterceptor;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.Clock;

@EnableSwagger2
@SpringBootApplication
public class SomeSpringApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(SomeSpringApplication.class);
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headShotInterceptor()).addPathPatterns("/**");
    }
    @Bean
    public HandlerInterceptor headShotInterceptor() {
        return new CustomHeaderInterceptor();
    }
//    @Bean
//    public Clock clock() {
//        return Clock.systemDefaultZone(); // from: LocalDateTime.now();
//    }

    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }
}
