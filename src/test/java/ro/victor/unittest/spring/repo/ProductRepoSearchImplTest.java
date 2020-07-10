package ro.victor.unittest.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
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
@ActiveProfiles({"db-real","dummyFileRepo"})
@Transactional
public class ProductRepoSearchImplTest {

   @Autowired
   private ProductRepo repo;

   @Before // foarte utila in app mari, cand nu stii cine lasa gunoi in DB
   public void checkDbIsEmpty() {
      assertThat(repo.findAll()).isEmpty();
   }

   @Test
   public void emptyCriteria() {
      ProductSearchCriteria criteria = new ProductSearchCriteria();
      repo.save(new Product().setSupplier(new Supplier()));
      List<ProductSearchResult> results = repo.search(criteria);
//      assertEquals(1, results.size()); // are mesaj de failure enervant
      assertThat(results).hasSize(1);
      repo.cevaUrat();

   }
   @Test
//   @Commit // pt debug - iti lasa testu sa comita
   public void byName() {
      ProductSearchCriteria criteria = new ProductSearchCriteria();
      criteria.name = "a";
      repo.save(new Product().setName("A"));
      List<ProductSearchResult> results = repo.search(criteria);
      assertThat(results).hasSize(1);

      repo.cevaUrat();
   }

}
