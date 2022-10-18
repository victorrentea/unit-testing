package victor.testing.spring.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
@SpringBootTest
@ActiveProfiles("db-mem")
public abstract class AbstractInMemDBTest {

}
