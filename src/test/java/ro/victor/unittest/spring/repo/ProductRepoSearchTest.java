package ro.victor.unittest.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"db-mem", "test"})
@Transactional
public class ProductRepoSearchTest {
   @Autowired
   private ProductRepo repo;

   private ProductSearchCriteria criteria = new ProductSearchCriteria();

   @Test
   public void emptyCriteria() {
      repo.save(new Product());
      assertThat(repo.search(criteria)).hasSize(1);
   }
   @Test
   public void byName() {
      repo.save(new Product("Masca De Protectie"));
      criteria.name = "dE";
      assertThat(repo.search(criteria)).hasSize(1);

      criteria.name = "notindb";
//      Assert.assertEquals(1, repo.search(criteria).size()); // mesaj de fail anost

      assertThat(repo.search(criteria)).hasSize(0);
      // mesaj de fail e mult mai sugestiv, ca face toString la elementele din lista

   }
}
