package victor.testing.spring.repo;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.SomeSpringApplication;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@CucumberContextConfiguration
@ActiveProfiles("db-mem")
@ContextConfiguration(
    classes = SomeSpringApplication.class,
    loader = SpringBootContextLoader.class
//    ,initializers = PostgresDBInitializer.class // TODO restore testcontainers + cucumber after migration from cucumber 1.x
)
public class ProductSearchSteps {

   // Can't use @DynamicPropertySource as this is not a @SpringBootTest
//   public static class PostgresDBInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//      @Override
//      public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//         TestPropertyValues.of(
//             "spring.datasource.url=" + ProductSearchFeature.postgres.getJdbcUrl(),
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

   @Given("^Supplier \"([^\"]*)\" exists$")
   @Transactional
   public void supplierExists(String supplierName) {
      log.debug("Persisting supplier {}", supplierName);
      supplierRepo.save(new Supplier().setName(supplierName));
   }

   @Given("^One product exists$")
   public void aProductExists() {
      product = new Product();
   }

   @And("^That product has name \"([^\"]*)\"$")
   public void thatProductHasName(String productName) {
      product.setName(productName);
   }

   @And("^That product has supplier \"([^\"]*)\"$")
   public void thatProductHasSupplier(String supplierName) {
      if (StringUtils.isNotBlank(supplierName)) {
         product.setSupplier(supplierRepo.findByName(supplierName));
      }
   }



   @When("^The search criteria name is \"([^\"]*)\"$")
   public void theSearchCriteriaNameIs(String productName) {
      criteria.name = productName;
   }

   @And("^The search criteria supplier is \"([^\"]*)\"$")
   public void theSearchCriteriaSupplierIs(String supplierName) {
      if (StringUtils.isNotBlank(supplierName)) {
         criteria.supplierId = supplierRepo.findByName(supplierName).getId();
      }
   }

   @Then("^That product is returned by search$")
   public void thatProductIsReturned() {
      productRepo.save(product);
      List<ProductSearchResult> results = productRepo.search(criteria);
      assertThat(results).hasSize(1);
      assertThat(results.get(0).getId()).isEqualTo(product.getId());
   }

   @Then("^No products are returned by search$")
   public void noProductsAreReturnedBySearch() {
      productRepo.save(product);
      List<ProductSearchResult> results = productRepo.search(criteria);
      assertThat(results).isEmpty();
   }

   @Then("^That product is returned by search: \"([^\"]*)\"$")
   public void thatProductIsReturnedBySearch(boolean found) throws Throwable {
      productRepo.save(product);
      List<ProductSearchResult> results = productRepo.search(criteria);
      if (found) {
         assertThat(results).hasSize(1);
      } else {
         assertThat(results).isEmpty();
      }
   }
}
