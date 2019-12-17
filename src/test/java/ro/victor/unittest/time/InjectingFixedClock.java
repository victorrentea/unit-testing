package ro.victor.unittest.time;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class InjectingFixedClock {
    public static final Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    @Autowired
    private OrderService service;

    @Test
    public void a() {
        LocalDateTime now = LocalDateTime.now(fixed);
        assertEquals(now.plusMinutes(10), service.m());
    }

}

@Configuration
class ClockConfig {
    @Bean
    @Profile("test")
    public Clock clock() {
        System.out.println("FAKE");
        return InjectingFixedClock.fixed;
    }
}

@SpringBootApplication
class FixedClockApp {
    @Bean
    @Profile("!test")
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}

@Service
class OrderService {
    @Autowired
    private Clock clock;

    public LocalDateTime m() {
        LocalDateTime now = LocalDateTime.now(clock);
        return now.plusMinutes(10);
    }

}