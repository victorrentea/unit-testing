package victor.testing.spring.repo.sub;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
//@ActiveProfiles("db-mem")
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // - NU are voie sa ajunga comisa pe GIT. O folosesti doar pe local sa faci de bugging: adica: cu @DC mai crapa testul asta rulat cu fratii ? daca NU -> leaking state a fost legata de spring: 95% - ceva ramas in DB din mem 5% state prin singleton sau cacheuri

@Transactional // >>>> RUPE ! 99% din testele relationale trebuie scrise asa
// NU merge pentru:
// a) PL/SQL care face COMMIT,
// b) cand inseri din alt thread(@Async, .submit),
// c) propagation=REQUIRES_NEW/NOT_SUPPORTED
// -- in cazurile de mai sus, renunta la @Transactional si fa cleanup cu:@Sql, ca mai jos
//@Sql(value = "/sql/cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class TestRepoBase {
 // 20 campuri protected 30 met utilitare 3-4 @Beforeuri si inca 3-4 super clase
}
