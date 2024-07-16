package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.entity.Product;
import victor.testing.tools.TestcontainersUtils;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Testcontainers
public class ProductRepoTestcontainersTest {
  static public PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:11");

  // TODO add in ~/.testcontainers.properties put testcontainers.reuse.enable=true

  @BeforeAll
  public static void startTestcontainer() {
    postgres.start();
  }

  @DynamicPropertySource
  public static void registerPgProperties(DynamicPropertyRegistry registry) {
    TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
  }

  @Autowired
  private ProductRepo repo;

  private ProductSearchCriteria criteria = new ProductSearchCriteria();

  @BeforeEach
  public void detectIncomingDataLeaks() {
    // good idea for larger projects
    assertThat(repo.count()).isEqualTo(0);
  }

  @Test
  public void noCriteria() {
    repo.save(new Product());
    assertThat(repo.search(criteria)).hasSize(1);
  }
}

