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

@SpringBootTest
@ActiveProfiles("db-mem")

// when still to use: @ConditionalOn > messes up the spring context
// state in some singleont pr > clear that state !
// caches > clear hte caches.
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // SHOULD BE REJECTED ON PUSH BY REMOTE. only use locally for debugging
// it forces Spring to start for every test. terrible for time.

//@Sql(scripts = "/sql/cleanup.sql")

@Transactional
public class ProductRepoTest {
   @Autowired
   ProductRepo repo;

   ProductSearchCriteria criteria = new ProductSearchCriteria();

   @Autowired
   SomeService someService;

//   @BeforeEach
//   final void before() {
//      repo.deleteAll(); // the best for nosql data stores (files, mongo, ...)
//   }

   @Test
   public void noCriteria() {
      repo.save(new Product("A"));
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(1);
      someService.method();
   }

   @Test
   public void noCriteriaBis() {
      repo.save(new Product("B"));
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(1);
   }
}

