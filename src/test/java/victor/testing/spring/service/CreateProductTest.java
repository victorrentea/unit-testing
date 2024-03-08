package victor.testing.spring.service;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyClient;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.service.ProductService;
import victor.testing.spring.api.dto.ProductDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.domain.ProductCategory.HOME;
//@ExtendWith(Spring//@ExtendWith(SpringExtension.class)Extension.class)
// in this case you are not starting the entire Spring but only a subset of beans
//@ContextConfiguration(classes = {ProductService.class})
// = using Spring only as a DI container, no @Transactions, no @GetMapping, no @Scheduled, @Async
// repos dont work
//@ContextConfiguration(classes = {ProductService.class})

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 9999) // startup another server in the JVM process running JUNIT
// that listenes on port 9999 serving requests from /mappings -> Picnic: MockServer

// #2 transactional test to auto rollback after each @Test
//@Transactional
// PROBLEMS: what if the tested code uses @Async, @Transactional(propagation = REQUIRES_NEW)
// limitations: you never see the COMMIT

// #3
//@Sql("classpath:/sql/cleanup.sql")
public class CreateProductTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo; // call a  PG in Docker
//  SafetyClient safetyClient;
   //a) mock the client
  // b) startup a MockServer in a Docker and call it < I'm gonna use a wiremock server
  @MockBean // uses mockito to create a mock and add that as a Bean for this type in your spring container
  KafkaTemplate<String, String> kafkaTemplate;

  @Autowired // feels like the test is a bean itself
  ProductService productService;
  private Long supplierId;

  // #1 cleanup manually. Perfect to Mongo, caches, kafka
//  @BeforeEach
//  @AfterEach
//  public void cleanup() {
//    productRepo.deleteAll();
//    supplierRepo.deleteAll();
//  }

  @Test
  void createThrowsForUnsafeProduct() {
//    when(safetyClient.isSafe("upc-unsafe")).thenReturn(false);
    ProductDto dto = new ProductDto("name", "upc-unsafe", -1L, HOME);

    assertThatThrownBy(() -> productService.createProduct(dto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @BeforeEach
  public final void insertCommonData() {
    supplierId = supplierRepo.save(new Supplier()).getId();
  }
  @Test
  void createOk() {
//    when(safetyClient.isSafe("upc-safe")).thenReturn(true);
    ProductDto dto = new ProductDto("name", "upc-safe", supplierId, HOME);

    // WHEN
    productService.createProduct(dto);

    Product product = productRepo.findAll().get(0);
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getUpc()).isEqualTo("upc-safe");
    assertThat(product.getSupplier().getId()).isEqualTo(supplierId);
    assertThat(product.getCategory()).isEqualTo(HOME);
    //assertThat(product.getCreatedDate()).isToday(); // field set via Spring Magic @CreatedDate
    //assertThat(product.getCreatedBy()).isEqualTo("user"); // field set via Spring Magic
    verify(kafkaTemplate).send(ProductService.PRODUCT_CREATED_TOPIC, "k", "NAME");
  }

}
