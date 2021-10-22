package victor.testing.spring.repo;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static victor.testing.tools.TestcontainersUtil.injectP6SPY;

@SpringBootTest
@Transactional
@Testcontainers
public class ProductRepoTestscontainersTest {
    @Autowired
    private ProductRepo repo;
    private ProductSearchCriteria criteria = new ProductSearchCriteria();

    @Container
    static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
        .withDatabaseName("prop")
        .withUsername("postgres")
        .withPassword("password");


    @SneakyThrows
    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> injectP6SPY(postgres.getJdbcUrl()));
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", ()-> "com.p6spy.engine.spy.P6SpyDriver");
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
    public void matchByName() {
        repo.save(new Product("A"));
        repo.save(new Product("B"));
        criteria.name="B";
        Assertions.assertThat(repo.search(criteria)).hasSize(1); // org.assertj:assertj-core:3.16.1
    }
//    @Test
//    public void noMatchByName() {
//        repo.save(new Product("B"));
//        criteria.name="A";
//        Assertions.assertThat(repo.search(criteria)).isEmpty(); // org.assertj:assertj-core:3.16.1
//    }

}

