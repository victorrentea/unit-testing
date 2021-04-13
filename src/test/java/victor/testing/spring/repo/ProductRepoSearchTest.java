package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepoSearchTest.Initializer;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
//@ActiveProfiles("db-mem")
@ContextConfiguration(initializers = Initializer.class)
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // do not ever push this to git
public class ProductRepoSearchTest {
   @Autowired
   private ProductRepo repo;
   @Autowired
   private SupplierRepo supplierRepo;

   @Container
   public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
       .withDatabaseName("integration-tests-db")
       .withUsername("sa")
       .withPassword("sa");

   static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
      public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
         TestPropertyValues.of(
             "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
             "spring.datasource.username=" + postgreSQLContainer.getUsername(),
             "spring.datasource.password=" + postgreSQLContainer.getPassword()
         ).applyTo(configurableApplicationContext.getEnvironment());
      }
   }
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

