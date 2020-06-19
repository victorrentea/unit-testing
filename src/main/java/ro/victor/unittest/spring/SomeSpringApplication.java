package ro.victor.unittest.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.repo.ProductRepo;
import ro.victor.unittest.spring.web.CustomHeaderInterceptor;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.Clock;

@EnableSwagger2
@SpringBootApplication
public class SomeSpringApplication implements WebMvcConfigurer, CommandLineRunner {
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
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone(); // from: LocalDateTime.now();
    }


    @Autowired
    private ProductRepo productRepo;
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        productRepo.save(new Product("Scaun la cap"));
    }
}
