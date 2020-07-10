package ro.victor.unittest.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@RepoTest
public class ProductRepoSearchImplTest extends RepoTestBase {

   @Autowired
   private ProductRepo repo;
   private final ProductSearchCriteria criteria = new ProductSearchCriteria();

   @Before // foarte utila in app mari, cand nu stii cine lasa gunoi in DB
   public void checkDbIsEmpty() {
      assertThat(repo.findAll()).isEmpty();
   }


   @Test
   public void emptyCriteria() {
      repo.save(new Product().setSupplier(new Supplier()));
      List<ProductSearchResult> results = repo.search(criteria);
//      assertEquals(1, results.size()); // are mesaj de failure enervant
      assertThat(results).hasSize(1);
//      repo.cevaUrat();

   }
   @Test
//   @Commit // pt debug - iti lasa testu sa comita
   public void byName() {
      repo.save(new Product().setName("xAy"));

      criteria.name = "xAy";
      assertThat(repo.search(criteria)).hasSize(1);

      criteria.name = "a";
      assertThat(repo.search(criteria)).hasSize(1);

      criteria.name = "notPresentInDB";
      assertThat(repo.search(criteria)).isEmpty();

//      repo.cevaUrat();

      // TransactionTemplate --<
//      repo.deleteAll();
       //daca vreun test acoper cod de prod care face COMMITURI, e politicos ca EL sa curete gunoiul in After
   }


}
