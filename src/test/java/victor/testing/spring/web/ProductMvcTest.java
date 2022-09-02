package victor.testing.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
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
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.TestcontainersUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("db-migration") // flyway
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999"
    //,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT//this starts a tomcat on a random port ,, NO!! BAD.
)
@Testcontainers
@AutoConfigureWireMock(port = 9999)
@Transactional

@AutoConfigureMockMvc // emulates the HTTP request without starting a Tomcat => faster + test and prod shares thread (is the same)
// 1) why do I NEED to share the thread?§
// 2) why would I NOT shjare the thread if I started a Tomcat and fired a RestTemplate call against it ?
    // tomcat assigns any incoming http requests to a random thread from its threadpool (200)
public class ProductMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SupplierRepo supplierRepo;

    private Long supplierId;

    @Container
    static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11");

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
    }

    @BeforeEach
    public void persistStaticData() {
        supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
    }

    @Test
    public void flowTest() throws Exception {
        createProduct("Tree");

        runSearch("{\"name\": \"Tree\"}", 1);
    }
    @Test
    public void flowTestLKike() throws Exception {
        createProduct("Tree");

        runSearch("{\"name\": \"re\"}", 1);
    }

    private void createProduct(String productName) throws Exception {
        // Option 1: JSON serialization (more convenient)
         ProductDto dto = new ProductDto(productName, "safebar", supplierId, ProductCategory.HOME);
         String createJson = new ObjectMapper().writeValueAsString(dto);

        // Option 2: Manual JSON formatting (more formal, "freezes" the DTO structure)
        // language=json
//        String createJson = """
//                {
//                    "name": "%s",
//                    "supplierId": "%d",
//                    "barcode": "safebar"
//                }
//                """.formatted(productName, supplierId);

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
