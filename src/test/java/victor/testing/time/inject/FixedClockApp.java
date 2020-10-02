package victor.testing.time.inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
public class FixedClockApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(FixedClockApp.class).close();
    }
    @Autowired
    private OrderService service;
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println();
        System.out.println();
        System.out.println("*** End of break at " + service.endOfBreak());
        System.out.println();
    }
}
