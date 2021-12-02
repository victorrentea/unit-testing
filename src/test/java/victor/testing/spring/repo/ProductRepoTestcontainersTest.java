package victor.testing.spring.repo;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;
//import static victor.testing.tools.TestcontainersUtil.injectP6SPY;

@Transactional
@SpringBootTest
@Testcontainers
public class ProductRepoTestcontainersTest {
   @Container
   static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
       .withDatabaseName("prop")
       .withUsername("postgres")
       .withPassword("password");
   @Autowired
   private ProductRepo repo;

   private ProductSearchCriteria criteria = new ProductSearchCriteria();


   @SneakyThrows
   @DynamicPropertySource
   public static void registerPgProperties(DynamicPropertyRegistry registry) {
      registry.add("spring.datasource.url", () -> /*injectP6SPY*/(postgres.getJdbcUrl()));
      registry.add("spring.datasource.driver-class-name", ()-> "com.p6spy.engine.spy.P6SpyDriver");

//      registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
//      registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

      registry.add("spring.datasource.username", postgres::getUsername);
      registry.add("spring.datasource.password", postgres::getPassword);
   }

   @BeforeEach
   public void initialize() {
      assertThat(repo.count()).isEqualTo(0); // good idea for larger projects
   }

   @Test
   public void noCriteria() {
      repo.save(new Product());
      assertThat(repo.search(criteria)).hasSize(1);
   }

   @Test
//    @Commit // for letting the Test Tx commit so that you can debug it after
   public void byNameMatch() {
      criteria.name = "Am";
      repo.save(new Product().setName("naMe"));
      assertThat(repo.search(criteria)).hasSize(1);
   }

   @Test
   public void byNameNoMatch() {
      criteria.name = "nameXX";
      repo.save(new Product().setName("name"));
      assertThat(repo.search(criteria)).isEmpty();
   }

//    @Test
//    public void bySupplierMatch() {
//        repo.save(new Product().setSupplier(commonData.getSupplier()));
//        criteria.supplierId = commonData.getSupplier().getId();
//        assertThat(repo.search(criteria)).hasSize(1);
//    }
//
//    @Test
//    public void bySupplierNoMatch() {
//        repo.save(new Product().setSupplier(commonData.getSupplier()));
//        criteria.supplierId = -1L;
//        assertThat(repo.search(criteria)).isEmpty();
//    }


   // TODO base test class persisting supplier

   // TODO replace with composition
}

