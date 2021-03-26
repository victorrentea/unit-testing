package victor.testing.spring.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRestTest {
   @MockBean
   private SafetyClient safetyClient;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductRepo productRepo;

   @Autowired
   private TestRestTemplate rest; // vs RestTemplate + base URL + .withBasicAuth("spring", "secret")
   private Long supplierId;
   //   private RestTemplate rest;

//   @Autowired
//   public void initRestTemplate(@Value("http://localhost:${local.server.port}") String baseUri) {
//      rest = new RestTemplate();
//      rest.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUri));
//   }

   @BeforeEach
   public void initialize() {
      productRepo.deleteAll();
      supplierRepo.deleteAll();
      supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
   }

   @Test
   public void testSearch() {
      when(safetyClient.isSafe("UPC")).thenReturn(true);

      ProductDto productDto = new ProductDto("Tree", "UPC", supplierId, ProductCategory.ME);

      ResponseEntity<Void> createResponse = rest.postForEntity("/product/create", productDto, Void.class);
      assertThat(createResponse.getStatusCode()).isEqualTo(OK);

      ProductSearchCriteria searchCriteria = new ProductSearchCriteria("Tree", null, null);

      ResponseEntity<List<ProductSearchResult>> searchResponse = rest.exchange(
          "/product/search", HttpMethod.POST,
          new HttpEntity<>(searchCriteria), new ParameterizedTypeReference<List<ProductSearchResult>>() {
          });


      List<ProductSearchResult> body = searchResponse.getBody();

      assertThat(searchResponse.getStatusCode()).isEqualTo(OK);
      assertThat(searchResponse.getBody()).hasSize(1);
      assertThat(searchResponse.getBody()).allMatch(p -> "Tree".equals(p.getName()));
   }


}
