package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.ProductCategory;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.service.subpacket.DBTestBase;
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.TestcontainersUtils;
import victor.testing.tools.WireMockExtension;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ActiveProfiles("db-mem") // no need anymore
@ActiveProfiles("db-migration") // flyway
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
@Testcontainers
public class ProductServiceTest extends DBTestBase {
//    @MockBean // replaces the real class with a mockito mock that you can then configur
//    public SafetyClient mockSafetyClient;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private ProductService productService;

    @RegisterExtension
    public WireMockExtension wireMock = new WireMockExtension(9999);

    private Long supplierId;

    @Container
    static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11");

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
    }

    @BeforeEach
    final void before() {
        supplierId = supplierRepo.save(new Supplier()).getId();
    }

    @Test
    public void createThrowsForUnsafeProduct() {
        ProductDto dto = new ProductDto("name", "bar", -1L, ProductCategory.HOME);

        assertThatThrownBy(() -> productService.createProduct(dto))
                .isInstanceOf(IllegalStateException.class);
    }
    @Test
    public void createOk() {
        ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

        // WHEN
        productService.createProduct(dto);

        assertThat(productRepo.count()).isEqualTo(1);
        Product product = productRepo.findAll().get(0);
        assertThat(product.getName()).isEqualTo("name");
        assertThat(product.getBarcode()).isEqualTo("safebar");
        assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
        assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
        assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
    }

}
