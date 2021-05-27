package victor.testing.spring.repo;

import eu.rekawek.toxiproxy.model.ToxicDirection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.containers.ToxiproxyContainer.ContainerProxy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.spring.tools.TestcontainersUtil.injectP6SPY;

@Transactional
@SpringBootTest
@ContextConfiguration//(initializers = PostgresDBInitializer.class)
@Testcontainers
public class ProductRepoTestcontainersTest {
   public static Network network = Network.newNetwork();
   @Container
   static public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
       .withDatabaseName("prop")
       .withUsername("postgres")
       .withPassword("password")
       .withNetwork(network);
   @Container
   static public ToxiproxyContainer toxiproxy = new ToxiproxyContainer("shopify/toxiproxy:2.1.0")
       .withNetworkAliases("toxiproxy")
       .withNetwork(network);
   @Autowired
   private ProductRepo repo;

   private ProductSearchCriteria criteria = new ProductSearchCriteria();

   @AfterAll
   public static void deleteNetwork() {
      network.close();
   }

   @SneakyThrows
   @DynamicPropertySource
   public static void registerPgProperties(DynamicPropertyRegistry registry) {
      System.out.println("Define Proxy ...");
      ToxiproxyContainer.ContainerProxy proxy = toxiproxy.getProxy(postgres, 5432);
      proxy.toxics().latency("latency", ToxicDirection.DOWNSTREAM, 10L);


      registry = injectP6SPY(registry);
      registry.add("spring.datasource.url", () -> getProxyedJdbcUrl(postgres.getJdbcUrl(), proxy));
//      registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
      registry.add("spring.datasource.username", () -> "postgres");
      registry.add("spring.datasource.password", () -> "password");
      registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
   }

   @SneakyThrows
   private static String getProxyedJdbcUrl(String originalJdbcUrl, ContainerProxy proxy) {
      System.out.println("Original jdbc url = " + originalJdbcUrl);
      String proxyHost = proxy.getContainerIpAddress() + ":" + proxy.getProxyPort();
      String proxiedJdbcUrl = "jdbc:postgresql://" + proxyHost + "/prop?loggerLevel=OFF";
      System.out.println("Proxied jdbc url = " + proxiedJdbcUrl);
      return proxiedJdbcUrl;
   }

   @BeforeEach
   public void initialize() {
      assertThat(repo.count()).isEqualTo(0); // good idea for larger projects
   }

   @Test
   public void noCriteria() {
      repo.save(new Product());
      assertThat(repo.search(criteria)).hasSize(1);
   }

   @Test
//    @Commit // for letting the Test Tx commit so that you can debug it after
   public void byNameMatch() {
      criteria.name = "Am";
      repo.save(new Product().setName("naMe"));
      assertThat(repo.search(criteria)).hasSize(1);
   }

   @Test
   public void byNameNoMatch() {
      criteria.name = "nameXX";
      repo.save(new Product().setName("name"));
      assertThat(repo.search(criteria)).isEmpty();
   }

//    @Test
//    public void bySupplierMatch() {
//        repo.save(new Product().setSupplier(commonData.getSupplier()));
//        criteria.supplierId = commonData.getSupplier().getId();
//        assertThat(repo.search(criteria)).hasSize(1);
//    }
//
//    @Test
//    public void bySupplierNoMatch() {
//        repo.save(new Product().setSupplier(commonData.getSupplier()));
//        criteria.supplierId = -1L;
//        assertThat(repo.search(criteria)).isEmpty();
//    }


   // TODO base test class persisting supplier

   // TODO replace with composition
}

