package victor.testing.spring.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.SupplierRepo;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional // unde sta tranzactia curenta stocata in java ? PE THREADUL CURENT! (LocalStorage)
// NU vreau Tomcat pornit pentru ca 1) nu merita mem/cpu 2) vreau sa raman pe acelasi thread, ca sa mearga @Transactional ROLLBACK
@SpringBootTest
@ActiveProfiles("db-mem")
@AutoConfigureMockMvc // EMULEAZA un tomcat: joaca rutarea si parsarea de JSON dar fara Tomcat.
public class ProductMvcBlackboxTest {
   @Autowired
   private MockMvc mockMvc;
   @MockBean
   private SafetyClient safetyClient;
   @Autowired
   private SupplierRepo supplierRepo;

   private Long supplierId;

   @BeforeEach
   public void persistStaticData() {
      supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
   }

   @Test
   public void testSearch() throws Exception {
      when(safetyClient.isSafe("safebar")).thenReturn(true);

      createProduct("Tree");
      runSearch("{\"name\": \"Tree\"}", 1);
   }
   @Test
   public void testSearch2() throws Exception {
      when(safetyClient.isSafe("safebar")).thenReturn(true);

      createProduct("Tree");
      runSearch("{\"name\": \"Copac\"}", 0);
   }

   // mai jos este un Testing DSL (un mini testing framework)
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
