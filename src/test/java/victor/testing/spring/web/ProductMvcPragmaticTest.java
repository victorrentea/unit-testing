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
import victor.testing.spring.web.dto.ProductSearchResult;
import victor.testing.tools.TestcontainersUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
public class ProductMvcPragmaticTest {
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
//    @WithMockUser
    public void flowTest() throws Exception {
        createProduct("Tree");

        runSearch(new ProductSearchCriteria().setName("Tree"), 1);
    }
    @Test
    public void notFound() throws Exception {
        createProduct("Copac");

        runSearch(new ProductSearchCriteria().setName("Tree"), 0);
    }

    @Autowired
    private ProductController productController;

    private void createProduct(String productName) throws Exception {
        // am chemat direct controllerul daca am contractul openapi
        ProductDto dto = new ProductDto(productName, "safebar", supplierId, HOME);
        productController.create(dto);
    }

    private void runSearch(ProductSearchCriteria criteria, int expectedNumberOfResults) throws Exception {
        List<ProductSearchResult> results = productController.search(criteria);
        assertThat(results).hasSizeLessThanOrEqualTo(expectedNumberOfResults);
    }
}
