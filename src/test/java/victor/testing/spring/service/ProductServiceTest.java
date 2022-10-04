package victor.testing.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
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
import victor.testing.spring.web.dto.ProductDto;
import victor.testing.tools.TestcontainersUtils;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional // limitari: @Async, @Transactional(REQUIRES_NEW/NOT_SUPPORTED)
//@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(properties = "safety.service.url.base=http://localhost:9999")
//@ActiveProfiles("db-mem") // rau ca : nu poti folosi SQL native dure, nu testezi flyway
@Testcontainers
@AutoConfigureWireMock(port=9999)
@ActiveProfiles("db-migration")
public class ProductServiceTest {

    @Container
    static public PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:11");

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
    }

//    @MockBean
//    public SafetyClient mockSafetyClient;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private ProductService productService;

    @Test
    public void createThrowsForUnsafeProduct() {
        ProductDto dto = new ProductDto("name", "bar", -1L, ProductCategory.HOME);
        assertThatThrownBy(() -> productService.createProduct(dto))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void createOk() {
        Long supplierId = supplierRepo.save(new Supplier()).getId();
        ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

        // WHEN
        productService.createProduct(dto);

        assertThat(productRepo.findAll()).hasSize(1); // supra testare!
        Product product = productRepo.findAll().get(0);
        assertThat(product.getName()).isEqualTo("name");
        assertThat(product.getBarcode()).isEqualTo("safebar");
        assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
        assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
        assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
    }

    @Test
    public void createOkBis() {
        Long supplierId = supplierRepo.save(new Supplier()).getId();
        ProductDto dto = new ProductDto("name", "safebar", supplierId, ProductCategory.HOME);

        // WHEN
        productService.createProduct(dto);

        assertThat(productRepo.findAll()).hasSize(1); // supra testare!
        Product product = productRepo.findAll().get(0);
        assertThat(product.getName()).isEqualTo("name");
        assertThat(product.getBarcode()).isEqualTo("safebar");
        assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
        assertThat(product.getCategory()).isEqualTo(ProductCategory.HOME);
        assertThat(product.getCreateDate()).isCloseTo(now(), byLessThan(1, SECONDS));
    }

}
