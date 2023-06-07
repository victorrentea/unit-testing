package victor.testing.spring.product.repo;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
//import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.SomeSpringApplication;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.api.dto.ProductSearchCriteria;
import victor.testing.spring.product.api.dto.ProductSearchResult;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
//@CucumberContextConfiguration // from io.cucumber:cucumber-spring:7.0.0 (commented out initially)
@ActiveProfiles("db-mem")
@ContextConfiguration(
    classes = SomeSpringApplication.class,
    loader = SpringBootContextLoader.class
//    ,initializers = ProductSearchSteps.PostgresDBInitializer.class // uncomment for Testcontainers integration
)
public class ProductSearchSteps {

//   public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:11")
//           .withDatabaseName("prop")
//           .withUsername("postgres")
//           .withPassword("password");
//   // Cannot use @DynamicPropertySource as this is not a @SpringBootTest
//   public static class PostgresDBInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//      @Override
//      public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//         System.out.println("Starting testcontainer");
//         container.start();
//         System.out.println("Testcontainer started");
//
//         TestPropertyValues.of(
//                 "spring.datasource.url=" + container.getJdbcUrl(),
//             "spring.datasource.username=postgres",
//             "spring.datasource.password=password",
//             "spring.datasource.driver-class-name=org.postgresql.Driver"
//         ).applyTo(configurableApplicationContext.getEnvironment());
//      }
//   }


   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;

   private ProductSearchCriteria criteria = new ProductSearchCriteria();

   private Product product;

   @Given("Supplier {string} exists")
   @Transactional
   public void supplierExists(String supplierName) {
      log.debug("Persisting supplier {}", supplierName);
      supplierRepo.save(new Supplier().setName(supplierName));
   }

   @Given("One product exists")
   public void aProductExists() {
      product = new Product();
   }

   @And("That product has name {string}")
   public void thatProductHasName(String productName) {
      product.setName(productName);
   }

   @And("That product has supplier {string}")
   public void thatProductHasSupplier(String supplierName) {
      if (StringUtils.isNotBlank(supplierName)) {
         product.setSupplier(supplierRepo.findByName(supplierName));
      }
   }

   @When("The search criteria name is {string}")
   public void theSearchCriteriaNameIs(String productName) {
      criteria.name = productName;
   }

   @And("The search criteria supplier is {string}")
   public void theSearchCriteriaSupplierIs(String supplierName) {
      if (StringUtils.isNotBlank(supplierName)) {
         criteria.supplierId = supplierRepo.findByName(supplierName).getId();
      }
   }

   @Then("That product is returned by search")
   public void thatProductIsReturned() {
      productRepo.save(product);
      List<ProductSearchResult> results = productRepo.search(criteria);
      assertThat(results).hasSize(1);
      assertThat(results.get(0).getId()).isEqualTo(product.getId());
   }

   @Then("No products are returned by search")
   public void noProductsAreReturnedBySearch() {
      productRepo.save(product);
      List<ProductSearchResult> results = productRepo.search(criteria);
      assertThat(results).isEmpty();
   }

   @Then("That product is returned by search: {string}")
   public void thatProductIsReturnedBySearch(String foundStr) throws Throwable {
      boolean found = Boolean.parseBoolean(foundStr);
      productRepo.save(product);
      List<ProductSearchResult> results = productRepo.search(criteria);
      if (found) {
         assertThat(results).hasSize(1);
      } else {
         assertThat(results).isEmpty();
      }
   }
}
