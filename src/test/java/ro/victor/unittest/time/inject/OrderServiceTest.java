package ro.victor.unittest.time.inject;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dummyFileRepo")
public class OrderServiceTest {
    @Autowired
    private OrderService service;

    @Test
    public void a() {
        LocalDateTime now = LocalDateTime.now(FixedClockTestConfig.fixedClock);
        assertEquals(now.plusMinutes(10), service.endOfBreak());
    }

}

