package victor.testing.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.ProductApi;
import victor.testing.spring.rest.dto.ProductDto;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;

//@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:test")
@ActiveProfiles("test")// sa incarc application.test.properties
@EmbeddedKafka // buteaza o kafka in memorie ~ H2
@SpringBootTest // porneste springu in procesul JUNit


// #2 pt schema legacy (multe tabele)
//@Sql(scripts = "/sql/cleanup.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)

// #3 auto-rollback: pt app noi cu JPA
@Transactional // ruleaza fiecare @Test intr-o tranzactie care este ROLLBACKED dupa fiecare test automat, ca sa lase baza curatar
@AutoConfigureWireMock(port = 0)
// doar pt library authors care scriu extenssi la spring (ninja teams)
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(roles = "ADMIN")
@AutoConfigureMockMvc
public class CreateProductTest extends BaseTest {
  @Autowired
  MockMvc mockMvc; // emulez apeluri REST catre app mea fara sa pornesc de fapt un tomcat :8080
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @MockBean
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @Autowired
  ProductApi productApi;
//  ProductService productService;

  @Test
  void createThrowsForUnsafeProduct() {
    ProductDto productDto = new ProductDto("name", "barcode-unsafe", "S", HOME);

    assertThatThrownBy(() -> productApi.create(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() throws URISyntaxException {
    supplierRepo.save(new Supplier().setCode("S"));
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", HOME);

    // WHEN
    productApi.create(productDto);

    Product product = productRepo.findAll().get(0);
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
    verify(kafkaTemplate).send(
        eq(ProductService.PRODUCT_CREATED_TOPIC),
        eq("k"),
        argThat(e -> e.productId().equals(product.getId())));
  }
  @Test
  void defaultsToUncategorizedForWithMissingCategory() throws URISyntaxException {
    supplierRepo.save(new Supplier().setCode("S"));
    ProductDto productDto = new ProductDto(
        "name", "barcode-safe", "S", null);

    productApi.create(productDto);

    Product product = productRepo.findAll().get(0);
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

  @Test
  void restCall() throws Exception {
    supplierRepo.save(new Supplier().setCode("S"));

    ProductDto productDto = new ProductDto(
        "name", "barcode-safe", "S", null);

    ObjectMapper objectMapper = new ObjectMapper();
////    productApi.create(productDto);
    mockMvc.perform(MockMvcRequestBuilders.post("/product/create")
        .content("""
            {
              "name": "name",
              "barcode": "barcode-safe",
              "supplierCode": "S"
            }
            """)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isCreated());

    Product product = productRepo.findAll().get(0);
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }

  @Test
  @WithMockUser(roles = "USER")
  void forbidden() throws URISyntaxException {
//    org.junit.jupiter.api.Assertions.assertThrows()// rau!
    Assertions.assertThatThrownBy(()->
        productApi.create(new ProductDto()))
    .isInstanceOf(AuthorizationDeniedException.class);
  }
}