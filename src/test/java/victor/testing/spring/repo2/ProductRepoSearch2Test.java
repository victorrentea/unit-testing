package victor.testing.spring.repo2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@ActiveProfiles("db-mem")
public class ProductRepoSearch2Test {
   @Autowired
   private ProductRepo repo;
   @Autowired
   private SupplierRepo supplierRepo;

   private ProductSearchCriteria criteria = new ProductSearchCriteria();

   @BeforeEach
   public final void before() {
      supplierRepo.deleteAll();
      repo.deleteAll();
   }

   @Test
   public void noCriteria() {
      repo.save(new Product());
      //save supplier
      assertThat(repo.search(criteria)).hasSize(1);
   }

   @Test
   public void noCriteria2() {
      repo.save(new Product());
      assertThat(repo.search(criteria)).hasSize(1);
   }

   // TODO finish

   // TODO base test class persisting supplier

   // TODO replace with composition
}

