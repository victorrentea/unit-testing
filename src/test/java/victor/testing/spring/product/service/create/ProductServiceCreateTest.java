package victor.testing.spring.product.service.create;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.BaseDatabaseTest;
import victor.testing.spring.product.domain.Product;
import victor.testing.spring.product.domain.Supplier;
import victor.testing.spring.product.infra.SafetyClient;
import victor.testing.spring.product.repo.ProductRepo;
import victor.testing.spring.product.repo.SupplierRepo;
import victor.testing.spring.product.service.ProductService;
import victor.testing.spring.product.api.dto.ProductDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.spring.product.domain.ProductCategory.HOME;
import static victor.testing.spring.product.domain.ProductCategory.UNCATEGORIZED;
// am uitat sa rulez TOATE TESTELE DECENTE INAINTE DE COMIT
//@Tag("slow")
//@SpringBootTest // porneste spring context
//@ActiveProfiles("db-mem")

//@Sql(scripts = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //

// Nukes Spring // cel mai antisocial lucru pe care-l poti face in test integrare:
// fiecare @Test adauga 30s la Jenkins build, ca reporneste spring cu tabelem ,,, hibern ,,,
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

@Transactional // in teste porneste o tranzactie separata / @Test, care dupa test este ROLLBACKed
// in acea tx ruleaza toate @Sql @BeforeEach (din super clase)
// + nu mai ai nevoie deloc de cleanup
// - nu merge daca codul testat nu lasa tranzactia de test sa intre:
//    - propagation=REQUIRES_NEW/NOT_SUPPORTED
//    - schimba threadul: @Async, executori, CompletableFuture, trimiti mesaj peste MQ
//  ==> N-ai ce sa faci decat sa renunti la @Transactional de pe test -> @BeforeEach cleanup
// - pericole: poti rata buguri: citeste : https://dev.to/henrykeys/don-t-use-transactional-in-tests-40eb

// Spring refoloseste contextul asta si la clasa cealalta. mai putin cand fac:
// - @ContextConfiguration(classes = TestConfig.class)
// - @ActiveProfiles("embedded-kafka")
//@TestPropertySource(properties = "prop=alta")
// seturi de @MockBean diferite
public class ProductServiceCreateTest extends BaseDatabaseTest {
//  @MockBean // inlocuieste beanul real SafetyClient cu un Mockito.mock() pe care ti-l pune si aici sa-l configurezi, auto-reset intre @Teste
//  SafetyClient safetyClient;
  @MockBean
  KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductService productService;

  @Test
  void explore() {

  }
}
