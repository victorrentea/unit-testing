package ro.victor.unittest.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"db-mem", "test"})
public class ProductRepoSearchTest {
   @Autowired
   private ProductRepo repo;
   @Autowired
   private SupplierRepo srepo;

   private ProductSearchCriteria criteria = new ProductSearchCriteria();

   @Before
   public void initialize() {
      srepo.deleteAll();
       repo.deleteAll();
   }

   @Test
   public void primuTest_cuOLumanarePeMasa() {
      repo.save(new Product().setSupplier(new Supplier()));
      assertThat(repo.search(criteria)).hasSize(1);
   }
   @Test
   public void primuTest_cuOLumanarePeMasa2() {
      repo.save(new Product().setSupplier(new Supplier()));
      assertThat(repo.search(criteria)).hasSize(1);
   }
}
