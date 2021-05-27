package victor.testing.spring.tools;

import org.springframework.test.context.DynamicPropertyRegistry;

public class TestcontainersUtil {
   public static DynamicPropertyRegistry injectP6SPY(DynamicPropertyRegistry registry) {
      return (name, valueSupplier) -> registry.add(name, () -> {
         switch (name) {
            case "spring.datasource.url":
               String originalUrl = (String) valueSupplier.get();
               String remainingUrl = originalUrl.substring("jdbc:".length());
               String p6spyUrl = "jdbc:p6spy:" + remainingUrl;
               System.out.println("Injected p6spy into jdbc url: " + p6spyUrl);
               return p6spyUrl;
            case "spring.datasource.driver-class-name":
               return "com.p6spy.engine.spy.P6SpyDriver";
            default:
               return valueSupplier.get();
         }
          }
      );
   }
}
