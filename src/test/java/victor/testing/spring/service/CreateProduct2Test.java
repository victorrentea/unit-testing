package victor.testing.spring.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import static java.time.LocalDateTime.now;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.*;
import static victor.testing.spring.entity.ProductCategory.HOME;
import static victor.testing.spring.entity.ProductCategory.UNCATEGORIZED;
import static victor.testing.spring.service.ProductService.PRODUCT_CREATED_TOPIC;

public class CreateProduct2Test extends ITest {
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  ProductService productService;

  @BeforeEach
  final void before() {
    supplierRepo.save(new Supplier().setCode("S"));
  }

  @Test
  void createThrowsForUnsafeProduct() {
    ProductDto productDto = new ProductDto("name", "barcode-unsafe", "S", HOME);

    assertThatThrownBy(() -> productService.createProduct(productDto))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Product is not safe!");
  }

  @Test
  void createOk() {
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", HOME);

    // WHEN
    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).get();
    assertThat(product.getName()).isEqualTo("name");
    assertThat(product.getBarcode()).isEqualTo("barcode-safe");
    assertThat(product.getSupplier().getCode()).isEqualTo("S");
    assertThat(product.getCategory()).isEqualTo(HOME);
  }
  /// ✄ -------- taie aici testul
  @Test
  void kafkaMessageSent() throws ExecutionException, InterruptedException, TimeoutException {

    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", HOME);
    String trace = "IO" + UUID.randomUUID().toString();
    MDC.put("traceid", trace);

    var newProductId = productService.createProduct(productDto);

    LocalDateTime startTime = now();
    var record = testListener.blockingReceive(
        r-> {
          Header header = r.headers().headers("traceid").iterator().next();
          String value = new String(header.value());
          return trace.equals(value);
        }, Duration.ofSeconds(1));
    assertThat(record.value().productId()).isEqualTo(newProductId);
    assertThat(record.value().observedAt()).isCloseTo(startTime, byLessThan(1, ChronoUnit.SECONDS));
  }

  @Test
  void defaultsToUncategorizedForMissingCategory() {
    ProductDto productDto = new ProductDto("name", "barcode-safe", "S", null);

    var newProductId = productService.createProduct(productDto);

    Product product = productRepo.findById(newProductId).get();
    assertThat(product.getCategory()).isEqualTo(UNCATEGORIZED);
  }



}