package victor.testing.spring.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.SupplierRepo;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional // ONE TRANSACTION TO RULE THEM ALL. in one tx: insert ref, create product web, search from web. rollback
@SpringBootTest
@AutoConfigureMockMvc // start Spring without tomcat and emulates HTTP requests to controller
public class ProductMvcBlackTest {
   @Autowired
   private MockMvc mockMvc; // can use this to 'emulate' web requests
   @MockBean
   private SafetyClient safetyClient; // mocking a bean
   @Autowired
   private SupplierRepo supplierRepo;

   private Long supplierId;

   @BeforeEach
   public void persistStaticData() {
      supplierId = supplierRepo.save(new Supplier().setActive(true)).getId(); // insert ref data
   }

   @Test
   public void testSearch() throws Exception {
      when(safetyClient.isSafe("safebar")).thenReturn(true);

      createProduct("Tree");

      /*List<ProductSearchResult> dtos = */
      runSearch("{\"name\": \"Tree\"}", 1);
   }

   private void createProduct(String productName) throws Exception {
      // Option 1: JSON serialization
      // ProductDto dto = new ProductDto(productName, "barcode", supplierId, ProductCategory.WIFE);
      // String createJson = new ObjectMapper().writeValueAsString(dto);

      // Option 2: Manual JSON formatting (freezes the DTO)
      // language=json
      String createJson = String.format("{\"name\": \"" + productName + "\", \"supplierId\": \"%d\", \"barcode\": \"safebar\"}", this.supplierId);

      mockMvc.perform(post("/product/create")
          .content(createJson)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
   }

   private void runSearch(String searchCriteriaJson, int expectedSize) throws Exception {
      mockMvc.perform(post("/product/search")
          .content(searchCriteriaJson)
          .contentType(MediaType.APPLICATION_JSON)
      )
          .andExpect(status().isOk())
          .andExpect(header().string("Custom-Header", "true"))
          .andExpect(jsonPath("$", hasSize(expectedSize)));
   }


}
