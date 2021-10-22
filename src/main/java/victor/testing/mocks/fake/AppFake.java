package victor.testing.mocks.fake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppFake {
   public static void main(String[] args) {
       SpringApplication.run(AppFake.class, args);
   }
}
