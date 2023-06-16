package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.BaseIntegrationTest;
import victor.testing.spring.product.api.dto.ProductDto;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;
@Transactional // se comporta altfel ca atunci cand il pui in prod: la final da rollback

public class CreateProduct2Test extends BaseIntegrationTest {
//  @MockBean
//  KafkaTemplate<String, String> kafkaTemplate;
//  @Autowired
//  ProductRepo productRepo;
//  @Autowired
//  SupplierRepo supplierRepo;
//  @Autowired
//  ProductService productService;
  @Autowired
  SafetyClient safetyClient;


  @Test
  void createThrowsForUnsafeProduct() {
    boolean result = safetyClient.isSafe("safebar");
    assertThat(result).isTrue();
  }

}
