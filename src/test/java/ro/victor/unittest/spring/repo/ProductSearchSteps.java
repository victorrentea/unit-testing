package ro.victor.unittest.spring.repo;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ro.victor.unittest.spring.SomeSpringApplication;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.domain.Product.Category;
import ro.victor.unittest.spring.domain.Supplier;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@ContextConfiguration(
        classes = SomeSpringApplication.class,
        loader = SpringBootContextLoader.class)
@ActiveProfiles("db-mem")
public class ProductSearchSteps {
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
        product.setSupplier(supplierRepo.findByName(supplierName));
    }

    @And("^That product has category \"([^\"]*)\"$")
    public void thatProductHasCategory(Category category) {
        product.setCategory(category);
    }


    @When("^The search criteria name is \"([^\"]*)\"$")
    public void theSearchCriteriaNameIs(String productName) {
        criteria.name = productName;
    }

    @And("^The search criteria supplier is \"([^\"]*)\"$")
    public void theSearchCriteriaSupplierIs(String supplierName) {
        criteria.supplierId = supplierRepo.findByName(supplierName).getId();
    }

    @And("^The search criteria category is \"([^\"]*)\"$")
    public void theSearchCriteriaCategoryIs(Category category) {
        criteria.category = category;
    }

    @Then("^That product is returned by search$")
    public void thatProductIsReturned() {
        productRepo.save(product);
        List<ProductSearchResult> results = productRepo.search(criteria);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).id).isEqualTo(product.getId());
    }

    @Then("^No products are returned by search$")
    public void noProductsAreReturnedBySearch() {
        productRepo.save(product);
        List<ProductSearchResult> results = productRepo.search(criteria);
        assertThat(results).isEmpty();
    }
}
