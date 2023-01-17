package victor.testing.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
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

import java.io.ObjectInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@ActiveProfiles("db-migration")
@Transactional
@Testcontainers
@AutoConfigureWireMock(port = 9999)

@AutoConfigureMockMvc // ❤️ emulates HTTP request without starting a Tomcat =>
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
    public void persistReferenceData() {
        supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
    }

    @Test
    public void flowTest() throws Exception {
        createProduct_json("Tree");

        searchProduct(new ProductSearchCriteria().setName("Tree"), 1);
    }

    // ==================== test-DSL (helpers/framework) ======================

    // -------- 1: raw JSON formatting -----
    // + freezes the contract (Dto structure + URL)
    // - cumbersome
    private void createProduct_json(String productName) throws Exception {
        // language=json
        String createJson = ("""
                {
                  "name": "%s",
                  "supplierId": "%d",
                  "barcode": "safebar",
                  "frate": 1,
                  "magie": true
                }
                """).formatted(productName, supplierId);

        mockMvc.perform(post("/product/create")
                        .content(createJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // -------- 2: Instantiate Dtos  ---------
    // + can test status code
    // ± robust against Dto structure change
    private void createProduct_dto(String productName) throws Exception {
        ProductDto dto = new ProductDto(productName, "safebar", supplierId, HOME);
        String createJson = jackson.writeValueAsString(dto);

        mockMvc.perform(post("/product/create")
                        .content(createJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // -------- 3: Direct controller method call ---------
    // > use if clients are using my openAPI contract or a client.jar
    // + shortest, testing 100% of MY LOGIC
    // ! Can be paired by an OpenAPI freeze test
    @Autowired
    private ProductController productController;
    private void createProduct_controllerCall(String productName) throws Exception {
        ProductDto dto = new ProductDto(productName, "safebar", supplierId, HOME);
        productController.create(dto);
    }

    private void searchProduct(ProductSearchCriteria criteria, int expectedNumberOfResults) throws Exception {
        mockMvc.perform(post("/product/search")
                        .content(jackson.writeValueAsString(criteria))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(header().string("Custom-Header", "true"));
    }
}
