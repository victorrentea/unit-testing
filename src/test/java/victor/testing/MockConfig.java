package victor.testing;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import victor.testing.spring.SomeSpringApplication;
import victor.testing.spring.feed.FileRepo;

@Configuration
@Import(SomeSpringApplication.class)
public class MockConfig {
//   @Bean
//   @Primary

   @MockBean
   FileRepo fileRepo;

   /// ++++
}
