package victor.testing.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.TestcontainersUtils;
import victor.testing.tools.WireMockExtension;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * - Connects to a production-like DB in a Docker image
 * - Runs the flyway migration scripts against this DB
 * - Uses WireMock to stub the JSON responses from third party APIs
 * - Starts one @Transaction / @Test
 * - Fills some 'static' data in the database (Supplier)
 * - Emulates a JSON request against my API and checks the JSON response
 * - At the end of each tests leaves the DB clean (by auto-rollback of @Transactional)
 */
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@Transactional
@Testcontainers
@ActiveProfiles("db-migration")
@AutoConfigureMockMvc // emulates the HTTP request without starting a Tomcat => faster + test and prod shares thread
public class ProductMvcBlackTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SupplierRepo supplierRepo;

    // === test containers ===
    @Container
    static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11");
    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
    }

    // === WireMock ===
    @RegisterExtension // starts-stops the WireMock web server that replies with pre-configured JSON responses
    public WireMockExtension wireMock = new WireMockExtension(9999);

    private Long supplierId;

    @BeforeEach
    public void persistStaticData() {
        supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
    }

    @Test
//    @WithMockUser(role="ala_din_OAUTH_token")
    public void flowTest() throws Exception {
        createProduct("Tree");

        runSearch("{\"name\": \"Tree\"}", 1);
    }

    private void createProduct(String productName) throws Exception {
        // Option 1: JSON serialization (more convenient) daca expui openapi
         ProductDto dto = new ProductDto(productName, "barcode", supplierId, ProductCategory.HOME);
         String createJson1 = new ObjectMapper().writeValueAsString(dto);

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
