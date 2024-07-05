package victor.testing.spring.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyApiClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.api.dto.ProductSearchCriteria;
import victor.testing.spring.api.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT) // starts a full Tomcat in memory
@ActiveProfiles({"db-mem", "embedded-kafka"})
@EmbeddedKafka(topics = "${input.topic}")
public class TomcatTest {
  @MockBean
  private SafetyApiClient safetyApiClient;
  @Autowired
  private SupplierRepo supplierRepo;
  @Autowired
  private ProductRepo productRepo;

  @Autowired
  private TestRestTemplate rest;
  private ProductSearchCriteria criteria = new ProductSearchCriteria();

  @BeforeEach
  public void initialize() {
    productRepo.deleteAll();
    supplierRepo.deleteAll();
    Long supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
    Product productInDB = new Product()
        .setName("Tree")
        .setBarcode("safe")
        .setSupplier(new Supplier().setId(supplierId))
        .setCategory(ProductCategory.ME);
    productRepo.save(productInDB);
  }

  @Test
  public void testSearch() {
    when(safetyApiClient.isSafe("safe")).thenReturn(true);

    ProductSearchCriteria searchCriteria = criteria.setName("Tree");

    ResponseEntity<List<ProductSearchResult>> searchResponse = rest.exchange(
        "/product/search", HttpMethod.POST,
        new HttpEntity<>(searchCriteria), new ParameterizedTypeReference<List<ProductSearchResult>>() {
        });

    assertThat(searchResponse.getStatusCode()).isEqualTo(OK);
    assertThat(searchResponse.getBody()).map(ProductSearchResult::getName).containsExactly("Tree");
  }


}
