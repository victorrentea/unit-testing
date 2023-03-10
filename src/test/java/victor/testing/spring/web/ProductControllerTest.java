package victor.testing.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
@AutoConfigureWireMock(port = 9999)
@Testcontainers
@ActiveProfiles("db-migration")
@Transactional

@AutoConfigureMockMvc // ❤️ emulates HTTP request without starting a Tomcat => @Transactional works, as the whole test shares 1 single thread
public abstract class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SupplierRepo supplierRepo;
    @Container
    static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11");

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
    }

    private final static ObjectMapper jackson = new ObjectMapper();

    private Long supplierId;

    @BeforeEach
    public void persistReferenceData() {
        supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
        dto = new ProductDto("productName", "safebar", supplierId, HOME);
    }

    @Test
    @WithMockUser(roles = "ADMIN") // current thread is ROLE_ADMIN
    public void flowTest() throws Exception {
        createProduct_json("Tree");
//        createProduct_dto("Tree");
//        createProduct_controllerCall(   "Tree");

        searchProduct(new ProductSearchCriteria().setName("Tree"), 1);
    }
    abstract void createProduct(String name);

    public static class Flavor1Test extends ProductControllerTest {
        @Override
        void createProduct(String name) {

        }
    }

    // ==================== test-DSL (helpers/framework) ======================


    // -------- 1: raw JSON formatting -----
    // + freezes the contract (Dto structure + URL)
    // - cumbersome
    private void createProduct_json(String productName) throws Exception {
        // language=json
        String createJson = """
                {
                    "name": null,
                    "supplierId": "%d",
                    "barcode": "safebar"
                }
                """.formatted( supplierId);

        mockMvc.perform(post("/product/create")
                        .content(createJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Custom-Header", "true"));
    }

    // -------- 2: Instantiate Dtos  ---------
    // + can test status code
    // ± robust against Dto structure change
    private ProductDto dto;

    private void createProduct_dto(String productName) throws Exception {
        String createJson = jackson.writeValueAsString(dto.setName(productName));

        mockMvc.perform(post("/product/create")
                        .content(createJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk()); // status
    }

    // -------- 3: Direct controller method call ---------
    // > use if clients are using my openAPI contract or a client.jar
    // + shortest, testing 100% of MY LOGIC
    // ! Can be paired by an OpenAPI freeze test
    @Autowired
    private ProductController productController;
    private void createProduct_controllerCall(String productName) {
        productController.create(dto.setName(productName));
    }

    @Test
    @WithMockUser
    public void createProductByNonAdmin_NotAuthorized() {
        assertThatThrownBy(() -> productController.create(dto))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createFails_forNullName() {
         productController.create(dto.setName(null));
    }

    private void searchProduct(ProductSearchCriteria criteria, int expectedNumberOfResults) throws Exception {
        mockMvc.perform(post("/product/search")
                        .content(jackson.writeValueAsString(criteria))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}
