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

@Transactional
@SpringBootTest
@ActiveProfiles("db-mem")
@AutoConfigureMockMvc // emulates the HTTP request without starting a Tomcat => faster + test and prod shares thread
public class ProductMvcTest {
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
    public void flowTest() throws Exception {
        when(safetyClient.isSafe("safebar")).thenReturn(true);

        createProduct("Tree");

        runSearch("{\"name\": \"Tree\"}", 1);
    }

    private void createProduct(String productName) throws Exception {
        // Option 1: JSON serialization (more convenient)
        // ProductDto dto = new ProductDto(productName, "barcode", supplierId, ProductCategory.WIFE);
        // String createJson = new ObjectMapper().writeValueAsString(dto);

        // Option 2: Manual JSON formatting (more formal, "freezes" the DTO structure)
        // language=json
        String createJson = """
                {
                    "name": "%s",
                    "supplierId": "%d",
                    "barcode": "safebar"
                }
                """.formatted(productName, supplierId);

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
