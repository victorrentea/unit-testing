package victor.testing.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.tools.WireMockExtension;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import javax.swing.text.html.FormSubmitEvent.MethodType;
import java.net.URI;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@Sql
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = "safety.service.url.base=http://localhost:9999")
@ActiveProfiles("db-real")
public class ProductRestTest {
   @Autowired
   private TestRestTemplate rest;
   @Autowired
   private SupplierRepo supplierRepo;

   @RegisterExtension
   public WireMockExtension wireMock = new WireMockExtension(9999);

   @Test
   public void testSearch() throws Exception {
      // date de referinta
      Long supplierId = supplierRepo.save(new Supplier()).getId(); // nu mai poate fi facut aici

      int r = new Random().nextInt(100);
      ProductDto dto = new ProductDto("Tree" + r, "SAFE", supplierId, ProductCategory.ME);

      RequestEntity<ProductDto> createRequest = RequestEntity.post(new URI("/product/create"))
          .contentType(MediaType.APPLICATION_JSON)
          .body(dto);

      ResponseEntity<Void> createReponse = rest.exchange(createRequest, Void.class);
      assertEquals(200, createReponse.getStatusCode().value());

      ProductSearchCriteria criteria = new ProductSearchCriteria("E" + r, null, null);

      RequestEntity<ProductSearchCriteria> searchRequest = RequestEntity.post(new URI("/product/search"))
          .contentType(MediaType.APPLICATION_JSON)
          .body(criteria);

      ResponseEntity<List<ProductSearchResult>> response = rest.exchange(searchRequest,
          new ParameterizedTypeReference<List<ProductSearchResult>>() {
          }
      );
      assertThat(response.getBody()).allMatch(result -> result.getName().equals("Tree" + r));

      // TODO
//        mockMvc.perform(delete("/product/"+idCreatMaiSus+"/delete")
//            .content()
//            .contentType(MediaType.APPLICATION_JSON)
//        )
   }
}
