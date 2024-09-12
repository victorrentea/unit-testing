package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.ProductCategory;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.infra.SafetyApiAdapter;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

@ExtendWith(MockitoExtension.class) // asta interpreteaza @ din clasa
class ProductServiceTest /*extends AltaSiAiaCuBefore*/{
  public static final String BARCODE = "#barcode#";
  public static final String SUPPLIER_CODE = "#supplierCode#";
  public static final long PRODUCT_ID = 13L;
  @Mock
  SafetyApiAdapter apiAdapter;
  @Mock
  SupplierRepo supplierRepo;
  @Mock
  ProductRepo productRepo;
  @Captor
  ArgumentCaptor<Product> productCaptor;
  @Mock
  KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  @InjectMocks
  ProductService target;

  // varianta canonica a datelor.
  // eventual merge pus la comun cu altii ca metoda statica in TestData
  // ContainerStack my = TestData.aContainerStack().withGps(constanta);
  ProductDto dto = new ProductDto() // 1 / @Test
      .setBarcode(BARCODE)
      .setCategory(HOME)
      .setSupplierCode(SUPPLIER_CODE);

  ProductServiceTest() {
    System.out.println("#sieu"); // beton
    //cate 1 instante per @Test =>
    // poti lasa gunoi in campuri de instanta
  }

  Supplier supplier = new Supplier();

  @BeforeEach
  final void setup() {
    // shared test fixture = contextul comun
    when(apiAdapter.isSafe(BARCODE)).thenReturn(true);
  }
//  @BeforeEach
//  final void setup2() {
//    // shared test fixture = contextul comun
//    when(apiAdapter.isSafe(BARCODE)).thenReturn(true);
//  }

  @Test
  void createThrows_forUnsafeProduct() {
    // reprogramat mockul
    when(apiAdapter.isSafe(BARCODE)).thenReturn(false);

    assertThrows(IllegalStateException.class, () ->
        target.createProduct(dto));
  }

  @Test
  void createProduct() {
    // given
    when(supplierRepo.findByCode(SUPPLIER_CODE)).thenReturn(of(supplier));
    when(productRepo.save(any())).thenReturn(new Product().setId(PRODUCT_ID));

    // when
    target.createProduct(dto);

    // then
    verify(productRepo).save(productCaptor.capture());
    Product product = productCaptor.getValue();
    assertThat(product.getCategory()).isEqualTo(HOME);
    ArgumentCaptor<ProductCreatedEvent> captor = forClass(ProductCreatedEvent.class);
    verify(kafkaTemplate).send(eq(PRODUCT_CREATED_TOPIC), eq("k"), captor.capture());
    ProductCreatedEvent event = captor.getValue();
    assertThat(event.productId()).isEqualTo(PRODUCT_ID);
    assertThat(event.observedAt()).isCloseTo(now(), byLessThan(1, SECONDS));
  }

  @Test
  void createProduct_mockuindTimpul() {
    when(supplierRepo.findByCode(SUPPLIER_CODE)).thenReturn(of(supplier));
    when(productRepo.save(any())).thenReturn(new Product().setId(PRODUCT_ID));
    LocalDateTime christmas = parse("2021-12-25T00:00:00");

    try (var staticMock = mockStatic(LocalDateTime.class)) {
      staticMock.when(LocalDateTime::now).thenReturn(christmas);
      target.createProduct(dto);
    }

    verify(kafkaTemplate).send(eq(PRODUCT_CREATED_TOPIC), eq("k"),
        argThat(event -> event.observedAt().equals(christmas)));
  }

  @Test
  void defaultToUncategorized() {
    dto.setCategory(null);
    when(supplierRepo.findByCode(SUPPLIER_CODE)).thenReturn(of(supplier));
    when(productRepo.save(any())).thenReturn(new Product().setId(PRODUCT_ID));

    target.createProduct(dto);

    verify(productRepo).save(argThat(product -> product.getCategory() == UNCATEGORIZED));
  }
}