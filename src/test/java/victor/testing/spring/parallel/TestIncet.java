package victor.testing.spring.parallel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TestIncet {
    @Test
    void unu() throws InterruptedException {
        log.info("Start1");
        Thread.sleep(5000);
        log.info("End1");
    }
    @Test
    void doi() throws InterruptedException {
        log.info("Start2");
        Thread.sleep(5000);
        log.info("End2");
    }
}
