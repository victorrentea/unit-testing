package victor.testing.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.JdbcDatabaseContainer;

@Slf4j
public class TestcontainersUtils {
    public static String injectP6SPYInJdbcUrl(String originalJdbcUrl) {
       String remainingUrl = originalJdbcUrl.substring("jdbc:".length());
       String p6spyUrl = "jdbc:p6spy:" + remainingUrl;
       log.info("Injected p6spy into jdbc url= " + p6spyUrl + "  (orignal= " + originalJdbcUrl+")");
       return p6spyUrl;
    }

    public static void addDatasourceDetails(DynamicPropertyRegistry registry, JdbcDatabaseContainer<?> databaseContainer, boolean p6spyEnabled) {
        if (p6spyEnabled) {
            // A: if you want to spy the JDBC calls
            registry.add("spring.datasource.url", () -> injectP6SPYInJdbcUrl(databaseContainer.getJdbcUrl()));
            registry.add("spring.datasource.driver-class-name", () -> "com.p6spy.engine.spy.P6SpyDriver");
        } else {
            // B: no spying
            registry.add("spring.datasource.url", databaseContainer::getJdbcUrl);
            registry.add("spring.datasource.driver-class-name", databaseContainer::getDriverClassName);
        }
        registry.add("spring.datasource.username", databaseContainer::getUsername);
        registry.add("spring.datasource.password", databaseContainer::getPassword);
    }
}
