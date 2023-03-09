package victor.testing.spring.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("db-mem")
public class ProductRepoTest {

   @Autowired
   ProductRepo repo;

   // this instance is re-creted for each @Test => no need for cleanup
   ProductSearchCriteria criteria = new ProductSearchCriteria();

   @Test
   public void noCriteria() {
      repo.save(new Product("A"));
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(1);
   }
   @Test
   public void byName() {
      repo.save(new Product("A"));
      criteria.name = "A";
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(1);
   }
   @Test
   public void byName_misMatch() {
      repo.save(new Product("A"));
      criteria.name = "B";
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(0);
   }

//   @Test
//   public void noCriteriaBis() {
//      repo.save(new Product("B"));
//      List<ProductSearchResult> results = repo.search(criteria);
//      assertThat(results).hasSize(1);
//   }
}

