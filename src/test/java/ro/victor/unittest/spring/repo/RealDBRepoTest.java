package ro.victor.unittest.spring.repo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@ActiveProfiles({"db-real","dummyFileRepo"})
@Transactional
public @interface RealDBRepoTest {

}
