package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.rest.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
//@Import(IntegrationTest.KafkaTestConfig.class)
//@ActiveProfiles("test")
//@EmbeddedKafka
//@AutoConfigureMockMvc
//@AutoConfigureWireMock(port = 0)
//@Transactional // ROLLBACK after each @Test

// slice-tests: 5s vs 9s @SpringBootTest
@DataJpaTest //booteaza doar partea de Hibernate + DB
public class ProductSearch1ITest {
  @Autowired
  ProductRepo repo;
  @Autowired
  SupplierRepo supplierRepo;

  long supplierId;

  ProductSearchCriteria criteria = new ProductSearchCriteria();

  @BeforeEach
  final void before() {
    repo.deleteAll();
    supplierRepo.deleteAll();
    supplierId = supplierRepo.save(new Supplier()).getId();
    repo.save(new Product()
            .setName("AbCd")
            .setSupplier(supplierRepo.getReferenceById(supplierId))
            .setCategory(ProductCategory.HOME)
    );
  }

  @Test
  public void noCriteria() {
    assertThat(repo.search(criteria)).hasSize(1);
  }

  @Test
  public void byName_noMatch() {
    criteria.name = "xyz";
    assertThat(repo.search(criteria)).hasSize(0);
  }

  @Test
  public void byName_matchExactly() {
    criteria.name = "AbCd";
    assertThat(repo.search(criteria)).hasSize(1);
  }

  @Test
  public void byName_matchLike() {
    criteria.name = "Bc";
    assertThat(repo.search(criteria)).hasSize(1);
  }

  // covered by Parameterized and .feature files
//  @Test
//  public void bySupplier_noMatch() {
//    criteria.supplierId = -1L;
//    assertThat(repo.search(criteria)).hasSize(0);
//  }
//
//  @Test
//  public void bySupplier_match() {
//    criteria.supplierId = supplierId;
//    assertThat(repo.search(criteria)).hasSize(1);
//  }
//
//  @Test
//  public void byCategory_noMatch() {
//    criteria.category = ProductCategory.ELECTRONICS;
//    assertThat(repo.search(criteria)).hasSize(0);
//  }
//
//  @Test
//  public void byCategory_match() {
//    criteria.category = ProductCategory.HOME;
//    assertThat(repo.search(criteria)).hasSize(1);
//  }
}

