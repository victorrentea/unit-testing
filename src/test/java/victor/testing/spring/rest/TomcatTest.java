package victor.testing.spring.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchCriteria.ProductSearchCriteriaBuilder;
import victor.testing.spring.rest.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@Disabled
@SpringBootTest(webEnvironment = RANDOM_PORT) // starts a full Tomcat in memory
public class TomcatTest extends IntegrationTest {
  @Autowired // points to the random port of Tomcaat
  private TestRestTemplate rest;
  private ProductSearchCriteriaBuilder criteria = ProductSearchCriteria.builder();
  @Autowired
  private ProductRepo productRepo;
  @Autowired
  private SupplierRepo supplierRepo;

  @BeforeEach
  public void initialize() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
    Long supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
    Product productInDB = new Product()
        .setName("Tree")
        .setBarcode("safe")
        .setSupplier(new Supplier().setId(supplierId))
        .setCategory(ProductCategory.HOME);
    productRepo.save(productInDB);
  }

  @Test
  public void search() {
    ProductSearchCriteria searchCriteria = criteria.name("Tree").build();

    ResponseEntity<List<ProductSearchResult>> searchResponse = rest.exchange(
        "/product/search", HttpMethod.POST,
        new HttpEntity<>(searchCriteria), new ParameterizedTypeReference<>() {
        });

    assertThat(searchResponse.getStatusCode()).isEqualTo(OK);
    assertThat(searchResponse.getBody()).map(ProductSearchResult::name).containsExactly("Tree");
  }


}
