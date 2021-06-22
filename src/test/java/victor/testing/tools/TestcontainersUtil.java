package victor.testing.tools;

import lombok.SneakyThrows;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.ToxiproxyContainer.ContainerProxy;

public class TestcontainersUtil {

   public static String injectP6SPY(String originalJdbcUrl) {
      String remainingUrl = originalJdbcUrl.substring("jdbc:".length());
      String p6spyUrl = "jdbc:p6spy:" + remainingUrl;
      System.out.println("Injected p6spy into jdbc url: " + p6spyUrl);
      return p6spyUrl;
   }

   @SneakyThrows
   public static String proxyJdbcUrl(JdbcDatabaseContainer<?> jdbcContainer, ContainerProxy proxy) {
      String originalJdbcUrl = jdbcContainer.getJdbcUrl();
      System.out.println("Original jdbc url = " + originalJdbcUrl);

      String proxyHost = proxy.getContainerIpAddress() + ":" + proxy.getProxyPort();
      String originalHost = jdbcContainer.getHost() + ":" + jdbcContainer.getFirstMappedPort();

//      String proxiedJdbcUrl = "jdbc:postgresql://" + proxyHost + "/prop?loggerLevel=OFF";
      String proxiedJdbcUrl = originalJdbcUrl.replace(originalHost, proxyHost);
      System.out.println("Proxied jdbc url = " + proxiedJdbcUrl);
      return proxiedJdbcUrl;
   }
}
