package victor.testing.spring.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@TestPropertySource(properties = {
//    "spring.datasource.url=jdbc:p6spy:h2:mem:",
//    "spring.datasource.driver-class-name=com.p6spy.engine.spy.P6SpyDriver",
//    "spring.datasource.username=sa",
//    "spring.datasource.password=sa",
//    "oneMoreProp=1",
//    "spring.jpa.hibernate.ddl-auto=create"
//})


@SpringBootTest
//@TestPropertySource(locations = "classpath:/application-db-mem.properties")
//@TestPropertySource(properties = {
//    "spring.datasource.url=jdbc:p6spy:h2:mem:",
//    "spring.datasource.driver-class-name=com.p6spy.engine.spy.P6SpyDriver",
//    "spring.datasource.username=sa",
//    "spring.datasource.password=sa",
//    "spring.jpa.hibernate.ddl-auto=create"
//})
@ActiveProfiles("db-mem") // using a real DB to run on > FLAKY tests > tests failing for non-bugs
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // 2 // NEVER push on remote> it's a waste of Jenkin's time. BAD PRACTICE ⚠

@Transactional //3 < THE BEST!!!
// spring boot test runner will start a new transaction for each @Test.
// in that transaciton it will run all [inherited] @BeforeEach + your test
// your tests passes or fails
// at the end the 'test' transaction is automatically rolled back by dfault


//@Sql(value = "/sql/cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
// 4 not recommended ifyour are uusing JPA. more for legacy databases.
public class ProductRepoTest {

   @Autowired
   ProductRepo repo;

   ProductSearchCriteria criteria = new ProductSearchCriteria();

//   @BeforeEach // 1
////   @AfterEach
//   public void clearDb() {
//      repo.deleteAll();
//   }

   @Test
   public void noCriteria() {
      repo.save(new Product("A"));
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(1);
   }

   @Test
   public void noCriteriaBis() {
      repo.save(new Product("B"));
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(1);
   }

}

// ONE PITFALL: When whould this mechanism FAIL? Data would remain in DB despite the rollback ?
// calling the func below will still COMMIT despite of the test transaction
//@Slf4j
//@RequiredArgsConstructor
//@Service
//class SomeService {
//   private ProductRepo productRepo;
//@Transactional(propagation = Propagation.REQUIRES_NEW)
//   public void method() {
//productRepo.save()
//   }
//}
