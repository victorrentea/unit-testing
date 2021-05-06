package victor.testing.spring.repo;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
public class ProductRepoTest {
    @Autowired
    private ProductRepo repo;

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

    private ProductSearchCriteria criteria = new ProductSearchCriteria();

//    @BeforeEach
//    public final void before() {
//        repo.deleteAll();
//    }
    @Autowired
    ProductService productService;
    private Supplier supplier;
    @Autowired
    private SupplierRepo supplierRepo;

    @BeforeEach
    public final void before() {
        assertThat(repo.findAll()).isEmpty();
        supplier = supplierRepo.save(new Supplier());
    }

    @Test
    public void noCriteria() {
        repo.save(new Product("A"));
        Assertions.assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
    @Test
    public void noCriteriaBis() {
        repo.save(new Product("B"));
        Assertions.assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
    @Test
    public void bySupplier() {
        repo.save(new Product("B").setSupplier(supplier));

        criteria.supplierId = supplier.getId();
        Assertions.assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1

        criteria.supplierId = -1L;
        Assertions.assertThat(repo.search(criteria)).isEmpty(); // org.assertj:assertj-core:3.16.1
    }

}

