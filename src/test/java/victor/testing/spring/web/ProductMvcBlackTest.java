package victor.testing.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
@AutoConfigureMockMvc // emuleaza tomcatul.  NU exista tomcat; mu deschide port
public class ProductMvcBlackTest {
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
      runSearch("{\"name\": \"Pom\"}", 0);
      createProduct("Tree");
      runSearch("{\"name\": \"Tree\"}", 2);
   }

   private void createProduct(String productName) throws Exception {
      // Option 1: JSON serialization
       ProductDto dto = new ProductDto(productName, "barcode", supplierId, ProductCategory.ME);
       String createJson = new ObjectMapper().writeValueAsString(dto);

      // Option 2: Manual JSON formatting (freezes the DTO)
      // language=json
//      String createJson = String.format("{\"name\": \"" + productName + "\", \"supplierId\": \"%d\", \"barcode\": \"safebar\"}", this.supplierId);

      mockMvc.perform(post("/product/create")
          .content(createJson)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
   }

   private void runSearch(String searchCriteriaJson, int expectedNumberOfResults) throws Exception {
      mockMvc.perform(post("/product/search")
          .content(searchCriteriaJson)
          .contentType(MediaType.APPLICATION_JSON)
      )
          .andExpect(status().isOk())
          .andExpect(header().string("Custom-Header", "true"))
          .andExpect(jsonPath("$", hasSize(expectedNumberOfResults)));
   }


}
