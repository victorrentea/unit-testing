package victor.testing.spring.service;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

// #2
//@Sql(scripts = "classpath:/sql/cleanup.sql", executionPhase = BEFORE_TEST_METHOD)

// criminal pt timpul de rulare al testelor pe CI => le furi zile din viata colegilor, si tie.
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) // #4☢️☢️☢️☢️☢️ DE EVITAT
// singura scuza ar fi sa cauti daca ceea ce "curge" intre teste este state dintr-un singleton spring
// eg: un cache
// daca dupa ce pui @DirtyContext nu mai pica testele => stii ca e state din spring.
// > curata acel state: eg @BeforeEach cleanCache() {cache.clear()}
// adica nu lasi @DirtiesContext pe Git ci il folosesti doar ca sa identifici problema

@SpringBootTest // porneste spring in procesul JVM al testelor, imposibil pt javaEE
@ActiveProfiles("test")
@EmbeddedKafka
@WithMockUser(username = "test-user", roles = "ADMIN")
@Transactional //#3 pus in teste cauzeaza rollback la finalul fiecarui test
// LIMITA: DB nu face niciodata COMMIT=>nu ruleaza (poate) niste @TransactionalEventListener(phase = AFTER_COMMIT) sau TRIGGER BEFORE COMMIT
public class CreateProductTest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockBean // spring inlocuieste beanul real cu un mock creat de Mockito
  SafetyApiAdapter safetyApiAdapter;
  @MockBean
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductService productService;
//  @BeforeEach // #1
//  final void cleanDB() {
//    productRepo.deleteAll(); // in ordinea FK
//    supplierRepo.deleteAll();
//  }

  @Test
  @WithMockUser(roles = "USER") // cand vrei doar user
  void failsForNonAdmin() {
    assertThatThrownBy(() -> productService.createProduct(new ProductDto()))
        .isInstanceOf(AuthorizationDeniedException.class);
  }

  @Test
  void createThrowsForUnsafeProduct() {
    when(safetyApiAdapter.isSafe("barcode-unsafe")).thenReturn(false);
    ProductDto productDto = new ProductDto("name", "barcode-unsafe", "S", HOME);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    supplierRepo.save(new Supplier().setCode("S"));
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", HOME);

    // WHEN
    Long newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCreatedDate()).isToday();
    assertThat(product.getCategory()).isEqualTo(HOME);
    assertThat(product.getCreatedBy()).isEqualTo("test-user");
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        argThat(e -> e.productId().equals(newProductId)));
  }

  @Test
  void defaultsToUncategorized() {
    supplierRepo.save(new Supplier().setCode("S"));
    when(safetyApiAdapter.isSafe("barcode-safe")).thenReturn(true);
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", null);

    // WHEN
    Long newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).orElseThrow();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }
}