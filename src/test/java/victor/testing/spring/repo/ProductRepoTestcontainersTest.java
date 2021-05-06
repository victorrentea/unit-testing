package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.WaitForDatabase;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@Testcontainers
public class ProductRepoTestcontainersTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
        .withDatabaseName("prop")
        .withUsername("postgres")
        .withPassword("password");

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", ()-> postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "password");
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Autowired
    private ProductRepo repo;
    @Autowired
    private SupplierRepo supplierRepo;

    private ProductSearchCriteria criteria = new ProductSearchCriteria();
    private final Supplier supplier = new Supplier();


    @BeforeEach
    public void initialize() {
        assertThat(repo.count()).isEqualTo(0); // good idea for larger projects
    }

    @BeforeEach
    public void insertSupplier() {
        supplierRepo.save(supplier);
    }
    @Test
    public void noCriteria() {
        repo.save(new Product());
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
    public void byNameNoMatch() {
        criteria.name = "X";
        repo.save(new Product().setName("name"));
        assertThat(repo.search(criteria)).isEmpty();
    }
    @Test
    public void byNameExactMatch() {
        criteria.name = "naMe";
        repo.save(new Product().setName("naMe"));
        assertThat(repo.search(criteria)).hasSize(1);
    }
    // TODO
    @Test
    public void byNameLikeMatch() {
        criteria.name = "Am";
        repo.save(new Product().setName("naMe"));
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
    public void bySupplierMatch() {
        repo.save(new Product().setSupplier(supplier));
        criteria.supplierId = supplier.getId();
        assertThat(repo.search(criteria)).hasSize(1);
    }

    @Test
    public void bySupplierNoMatch() {
        repo.save(new Product().setSupplier(supplier));
        criteria.supplierId = -1L;
        assertThat(repo.search(criteria)).isEmpty();
    }


    // TODO base test class persisting supplier

    // TODO replace with composition
}

