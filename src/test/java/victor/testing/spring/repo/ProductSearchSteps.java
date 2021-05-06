package victor.testing.spring.repo;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import victor.testing.spring.SomeSpringApplication;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.ProductSearchSteps.PostgresDBInitializer;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Slf4j
@ContextConfiguration(
    classes = SomeSpringApplication.class,
    loader = SpringBootContextLoader.class,
    initializers = PostgresDBInitializer.class)
public class ProductSearchSteps {

   @When("^The search criteria name is \"([^\"]*)\"$")
   public void theSearchCriteriaNameIs(String searchName) throws Throwable {
      criteria.name = searchName;
   }

   @And("^The search criteria supplier is \"([^\"]*)\"$")
   public void theSearchCriteriaSupplierIs(String supplierName) throws Throwable {
      if (supplierName != null && !supplierName.isEmpty()) {
         criteria.supplierId = supplierRepo.findByName(supplierName).getId();
      }
   }

   // Can't use @DynamicPropertySource as this is not a @SpringBootTest
   public static class PostgresDBInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
      @Override
         public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + ProductSearchFeature.postgres.getJdbcUrl(),
                "spring.datasource.username=postgres",
                "spring.datasource.password=password",
                "spring.datasource.driver-class-name=org.postgresql.Driver"
            ).applyTo(configurableApplicationContext.getEnvironment());
         }
   }



   @Autowired
   private ProductRepo productRepo;
   @Autowired
   private SupplierRepo supplierRepo;

   private ProductSearchCriteria criteria = new ProductSearchCriteria();

   private Product product;

   @Given("^Supplier \"([^\"]*)\" exists$")
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
