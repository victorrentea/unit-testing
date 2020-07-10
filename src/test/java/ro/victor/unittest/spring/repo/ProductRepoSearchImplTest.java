package ro.victor.unittest.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles({"db-mem","dummyFileRepo"})
@Transactional
public class ProductRepoSearchImplTest {

   @Autowired
   private ProductRepo repo;

   @Test
   public void emptyCriteria() {
      ProductSearchCriteria criteria = new ProductSearchCriteria();
      repo.save(new Product().setSupplier(new Supplier()));
      List<ProductSearchResult> results = repo.search(criteria);
//      assertEquals(1, results.size()); // are mesaj de failure enervant
      assertThat(results).hasSize(1);
   }
   @Test
   public void byName() {
      ProductSearchCriteria criteria = new ProductSearchCriteria();
      criteria.name = "a";
      repo.save(new Product().setName("a"));
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(1);
   }

}
