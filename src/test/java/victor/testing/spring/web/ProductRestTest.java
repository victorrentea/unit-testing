package victor.testing.spring.web;

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
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ProductRestTest {
   @MockBean
   private SafetyClient safetyClient;
   @Autowired
   private SupplierRepo supplierRepo;
   @Autowired
   private ProductRepo productRepo;

   @Autowired
   private TestRestTemplate rest;
   private Long supplierId;
   private ProductSearchCriteria criteria = new ProductSearchCriteria();

   @BeforeEach
   public void initialize() {
      productRepo.deleteAll();
      supplierRepo.deleteAll();
      supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
   }

   @Test
   public void testSearch() {
      when(safetyClient.isSafe("safebar")).thenReturn(true);

      ProductDto productDto = new ProductDto("Tree", "safebar", supplierId, ProductCategory.ME);

      ResponseEntity<Void> createResponse = rest.postForEntity("/product/create", productDto, Void.class);
      assertThat(createResponse.getStatusCode()).isEqualTo(OK);

      ProductSearchCriteria searchCriteria = criteria.setName("Tree");

      ResponseEntity<List<ProductSearchResult>> searchResponse = rest.exchange(
          "/product/search", HttpMethod.POST,
          new HttpEntity<>(searchCriteria), new ParameterizedTypeReference<List<ProductSearchResult>>() {
          });

      assertThat(searchResponse.getStatusCode()).isEqualTo(OK);
      assertThat(searchResponse.getBody().stream().map(ProductSearchResult::getName)).containsExactly("Tree");
   }


}
