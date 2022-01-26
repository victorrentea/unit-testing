package victor.testing.spring.repo.sub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "prop=change")
//@SpringBootTest
//@ActiveProfiles("db-mem")
//@Transactional
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // waste of Jenkins time. Never push on git, only gfor debugging or when the context has some state @Conditiona, stateful singletons having fields (eg counters), @Cacheable
class ProductRepoSearch2ImplTest {
   @Autowired
   ProductRepo repo;


   ProductSearchCriteria criteria = new ProductSearchCriteria();

//   @BeforeEach
//   final void before() {
//      repo.deleteAll(); // THE way to go if talking to Embedded Mongo/ cassandra/in mem kafka
//   }
   @Test
//   @Sql(value = "/sql/cleanup.sql")
   void search() {
      repo.save(new Product().setName("Tree"));

      List<ProductSearchResult> results = repo.search(criteria);

      assertThat(results).hasSize(1);
   }

   @Test
   void search2() {
      repo.save(new Product().setName("Tree2"));

      List<ProductSearchResult> results = repo.search(criteria);

      assertThat(results).hasSize(1);
   }
}

//// somewhere in production someone did
//@Slf4j
//@RequiredArgsConstructor
//@Service
//class MyService {
//   private MyDep myDep;
//
//   @Transactional(propagation = Propagation.REQUIRES_NEW)
//   public void method() {
//
//   }
//}