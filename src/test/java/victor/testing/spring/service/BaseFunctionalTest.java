package victor.testing.spring.service;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("db-migration")
public abstract class BaseFunctionalTest {
}
