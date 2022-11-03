//package victor.testing.spring.web;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Configuration
//public class CustomHeaderConfig implements WebMvcConfigurer {
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(headShotInterceptor()).addPathPatterns("/**");
//    }
//
//    @Bean
//    public HandlerInterceptor headShotInterceptor() {
//        return new CustomHeaderInterceptor();
//    }
//
//    @Slf4j
//    public static class CustomHeaderInterceptor implements HandlerInterceptor {
//        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//            log.debug("HTTP Interceptor adding custom header to response");
//            response.addHeader("Custom-Header", "true");
//            return true;
//        }
//    }
//
//}
