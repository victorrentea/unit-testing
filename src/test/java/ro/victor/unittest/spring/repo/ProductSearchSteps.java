package ro.victor.unittest.spring.repo;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
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
public class ProductSearchSteps {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private SupplierRepo supplierRepo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    private Product product;


    @Given("^A product exists in database$")
    public void aProductExistsInDatabase() {
        product = new Product();
        productRepo.save(product);
    }

    @And("^That product name is \"([^\"]*)\"$")
    public void thatProductNameIs(String productName) throws Throwable {
        product.setName(productName);
    }

    @When("^Search criteria name is \"([^\"]*)\"$")
    public void searchCriteriaNameIs(String searchName) throws Throwable {
        criteria.name = searchName;
    }

    @Then("^That product is returned$")
    public void thatProductIsReturned() {
        List<ProductSearchResult> results = productRepo.search(criteria);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(product.getId());
    }
}
