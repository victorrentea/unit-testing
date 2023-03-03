package victor.testing.spring.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import victor.testing.tools.TestcontainersUtils;

//@Primary
//@Profile("test")
//@Component
//class AlternativeImplemForTests  implements AnInterfFromProd{
//
//}

@ActiveProfiles({"db-mem","test"})
@SpringBootTest
//@Testcontainers
//@Sql(value = "classpath:/sql/cleanup.sql",
//        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class BaseTest {
//  @Container
//  static PostgreSQLContainer<?> postgres =
//          new PostgreSQLContainer<>("postgres:11");
//
//  @BeforeEach
//  public void startTestcontainer() {
//    postgres.start();// TODO exercise for the reader: how to start docker ONCE for all tests!
//  }

//  @DynamicPropertySource
//  public static void registerPgProperties(DynamicPropertyRegistry registry) {
//    TestcontainersUtils.addDatasourceDetails(registry, postgres, true);
//  }
}
