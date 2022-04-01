package victor.testing.spring.repo.sub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"db-mem","in-mem-kafka"})
// TODO
//@Sql(value = "classpath:/sql/common-reference-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductRepoTest {
   @Autowired
   ProductRepo repo;

   ProductSearchCriteria criteria = new ProductSearchCriteria();

//   @BeforeAll TODO
//   public void beforeTheWholeClass() {
////      insert heavy
//   }
//   @AfterAll
//   public void afterTheWholeClass() {
////      DELETE heavy
//   }
//   @BeforeEach // before not after because we do not trust that others cleaned after them : we clean what we need to clean
//   final void cleanDB() {
//      repo.deleteAll();
//   }

   @Test
   public void noCriteria() {
      Product product = new Product("A");
      repo.save(product);
      List<ProductSearchResult> results = repo.search(criteria);
//      assertThat(results).hasSize(1);
      assertThat(results)
//          .anyMatch(p -> product.getId().equals(p.getId()))
          .hasSize(1)
      ;
   }

   @Test
   public void noCriteriaBis() { // not isolated tests
      repo.save(new Product("B"));
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(1)
//          .anyMatch(p -> "B".equals(p.getName()))
      ;
   }
}

