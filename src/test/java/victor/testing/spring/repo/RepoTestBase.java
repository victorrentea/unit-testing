package victor.testing.spring.repo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.repo.RepoTestBase.DockerDataSourceInitializer;

//@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional

@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = DockerDataSourceInitializer.class)
public abstract class RepoTestBase {
   @Autowired
   protected SupplierRepo supplierRepo;
   protected Supplier supplier;


//  @Before
//  public final void before() {
//
//  }


   public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:9.4");
//   public static OracleContainer container = new OracleContainer("oracle/database:18.4.0-xe");

   static {
      container.start();
   }

   public static class DockerDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

      @Override
      public void initialize(ConfigurableApplicationContext applicationContext) {

         TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
             applicationContext,
             "spring.datasource.url=" + insertP6Spy(container.getJdbcUrl()),
             "spring.datasource.username=" + container.getUsername(),
//                "spring.datasource.driver-class-name=org.postgresql.Driver",
             "spring.datasource.driver-class-name=com.p6spy.engine.spy.P6SpyDriver",
             "spring.datasource.password=" + container.getPassword()
         );
      }
   }
   public static String insertP6Spy(String jdbcUrl) {
      return "jdbc:p6spy:" + jdbcUrl.substring("jdbc:".length());
   }


   @BeforeEach
   public final void initDataInDB() {
      supplier = supplierRepo.save(new Supplier());
   }
}
