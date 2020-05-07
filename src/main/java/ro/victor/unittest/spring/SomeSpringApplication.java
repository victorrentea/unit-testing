package ro.victor.unittest.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Clock;
import java.time.LocalDateTime;

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
        return new HandlerInterceptor() {
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                System.out.println("INTERCEPTOR RUNS");
                response.addHeader("Head-Shot", "true");
                return true;
            }
        };
    }
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone(); // from: LocalDateTime.now();
    }
}
