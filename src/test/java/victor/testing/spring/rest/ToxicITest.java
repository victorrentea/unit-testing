package victor.testing.spring.rest;

import eu.rekawek.toxiproxy.model.Toxic;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.ToxiproxyContainer;
import victor.testing.spring.IntegrationTest;
import victor.testing.spring.entity.Product;
import victor.testing.spring.entity.Supplier;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.repo.SupplierRepo;
import victor.testing.spring.rest.dto.ProductSearchCriteria;
import victor.testing.spring.rest.dto.ProductSearchResult;

import java.io.IOException;

import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.entity.ProductCategory.HOME;

@Slf4j
@Transactional
public class ToxicITest extends IntegrationTest {
  public static final String PRODUCT_NAME = "AbCd";
  @Autowired
  ProductRepo repo;
  @Autowired
  SupplierRepo supplierRepo;
  @Autowired
  ApiTestDSL api;
  @Autowired
  ToxiproxyContainer.ContainerProxy postgresqlContainerProxy;

  @Test
  void search() throws IOException {
    postgresqlContainerProxy.toxics()
        .latency("latency",ToxicDirection.DOWNSTREAM, 1000);

    repo.deleteAll();

    long supplierId = supplierRepo.save(new Supplier()).getId();
    Long productId = repo.save(new Product()
        .setName(PRODUCT_NAME)
        .setSupplier(supplierRepo.getReferenceById(supplierId))
        .setCategory(HOME)
    ).getId();

    long t0 = currentTimeMillis();
    var searchResults = api.searchProduct(ProductSearchCriteria.empty());
    long t1 = currentTimeMillis();
    for (Toxic toxic : postgresqlContainerProxy.toxics().getAll()) toxic.remove();
    log.info("Search took {}ms", t1 - t0);

    assertThat(searchResults).containsExactly(new ProductSearchResult(productId, PRODUCT_NAME));
  }
}

