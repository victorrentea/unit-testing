package ro.victor.unittest.spring.facade;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("db-mem")
public abstract class RepoTest {
}
