package victor.testing.spring.parallel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TestIncet2AltaClasa {
    @Test
    void trei() throws InterruptedException {
        log.info("Start3");
        Thread.sleep(1000);
        log.info("End3");
    }

}
