package victor.testing.spring.parallel;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.cache.ContextCache;
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate;

import java.lang.reflect.Field;

@Slf4j
@DataJpaTest
@ActiveProfiles("db-mem")
//@SpringBootTest
public class TestIncet2AltaClasa {
    @SneakyThrows
    @Test
    void trei() throws InterruptedException {
        log.info("Start3");
        Thread.sleep(1000);
        log.info("End3");


    }
    // 31.3 ... 37.5 -- 6.2 sec -- 3.2s: @SpringBootTest
    // 19.9 .. 24.9 -- 5 -- 2s  sec @DataJPa

}
