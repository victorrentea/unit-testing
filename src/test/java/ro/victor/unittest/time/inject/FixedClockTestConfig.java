package ro.victor.unittest.time.inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
@Profile("test")
public class FixedClockTestConfig {
    public static final Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    @Bean
    @Primary
    public Clock testClock() {
        System.out.println("FAKE");
        return fixedClock;
    }
}
