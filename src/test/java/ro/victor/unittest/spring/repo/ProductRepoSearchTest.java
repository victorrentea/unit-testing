package ro.victor.unittest.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"db-mem", "test"})
public class ProductRepoSearchTest {
   @Autowired
   private ProductRepo repo;

   private ProductSearchCriteria criteria = new ProductSearchCriteria();

   @Test
   public void primuTest_cuOLumanarePeMasa() {
      repo.save(new Product());
      assertThat(repo.search(criteria)).hasSize(1);
   }
}
