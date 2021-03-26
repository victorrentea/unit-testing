package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductRepoSearchTest extends AbstractRepoTestBase {
   @Autowired
   private ProductRepo repo;
   @Autowired
   private SomeService someService;
   private ProductSearchCriteria criteria = new ProductSearchCriteria();

   //    @BeforeEach
//    public final void before() {
//        repo.deleteAll();
//    }
   @Test
   public void noCriteria() {
      repo.save(new Product("Tree"));

      List<ProductSearchResult> list = repo.search(criteria);
      someService.method();
      assertThat(list).hasSize(1);
   }

   @Test
   public void noCriteria2() {
      repo.save(new Product("Tree"));

      List<ProductSearchResult> list = repo.search(criteria);
      someService.method();
      assertThat(list).hasSize(1);
   }

   // TODO finish

   // TODO base test class persisting supplier

   // TODO replace with composition
}

