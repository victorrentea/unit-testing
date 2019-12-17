package ro.victor.unittest.time;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class InjectingFixedClock2 {
    public static final Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    @Autowired
    private OrderService service;

    @Test
    public void a() {
        LocalDateTime now = LocalDateTime.now(fixed);
        assertEquals(now.plusMinutes(10), service.m());
    }

    @Test
    public void b() {
        LocalDateTime now = LocalDateTime.now(fixed);
        assertEquals(now.plusMinutes(10), service.m());
    }

}
