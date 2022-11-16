package victor.testing.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.tools.TestcontainersUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static victor.testing.spring.domain.ProductCategory.HOME;

/**
 * <li> Connects to a production-like DB in a Docker image
 * <li> Runs the flyway migration scripts against this DB
 * <li> Uses WireMock to stub the JSON responses from third party APIs
 * <li> Starts one @Transaction / @Test
 * <li> Fills some 'static' data in the database (Supplier)
 * <li> Emulates a JSON request against my API and checks the JSON response
 * <li> At the end of each tests leaves the DB clean (by auto-rollback of @Transactional)
 */
@Transactional
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@Testcontainers
@ActiveProfiles("db-migration")
@AutoConfigureWireMock(port = 9999)
@AutoConfigureMockMvc // ❤️ emulates HTTP request without starting a Tomcat. WHY? I don't want my tests to fire a real HTTP request => that would loose my thread
    // => @Transactional works, as the whole test shares 1 single thread
public class ProductMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SupplierRepo supplierRepo;

    // === Testcontainers ===
    @Container
    static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11");

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
    }

    private final ObjectMapper jackson = new ObjectMapper();

    private Long supplierId;

    @BeforeEach
    public void persistStaticData() {
        supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
    }

    @Test
    public void flowTest() throws Exception {
        createProductStrictJSON("Tree");

        runSearch(new ProductSearchCriteria().setName("Tree"), 1);
    }

    private void createProductStrictJSON(String productName) throws Exception {

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
    private void createProductUsingDtos(String productName) throws Exception {
        // your test might pass but your consumers might cakll you angry: your broke the JSON structure !!!
        // BUT if we could make sure all our client have the correct Dtos, this test would be better
        // -> how to do that ? -> openapi /v3/api

        // API-level test in memory firing real JSON String  -> the most strict test you can write
        // Option 1: JSON serialization (more convenient)
        ProductDto dto = new ProductDto(productName, "safebar", supplierId, HOME);
        String createJson = jackson.writeValueAsString(dto);

        mockMvc.perform(post("/product/create")
                        .content(createJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Autowired
    private ProductController productController;

    private void createProductUsingDirectControllerMethodCall(String productName) throws Exception {
        productController.create(new ProductDto(productName, "safebar", supplierId, HOME));
    }

    // Victor, we keep a copy of our production open-api contract from the code
    // (v3/api) in src/test/resources/my-api.json
    // and we have a test that starts @SpringBootTest and calls /v3/api to check the api stays the same.

    private void runSearch(ProductSearchCriteria criteria, int expectedNumberOfResults) throws Exception {
        mockMvc.perform(post("/product/search")
                        .content(jackson.writeValueAsString(criteria))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(header().string("Custom-Header", "true"))
                .andExpect(jsonPath("$", hasSize(expectedNumberOfResults)));
    }
}
