package victor.testing.spring;

import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WaitForDBInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

   @Override
   public void initialize(ConfigurableApplicationContext applicationContext) {
      System.out.println("Before Anything " + applicationContext.getEnvironment().getProperty("spring.datasource.url"));
      Awaitility.await()
          .atLeast(10, TimeUnit.SECONDS)
          .pollInterval(1, TimeUnit.SECONDS)
          .atMost(1, TimeUnit.MINUTES)
          .until(() -> canConnectToDB(applicationContext.getEnvironment()));
   }

   private boolean canConnectToDB(Environment env) {
      String url = env.getRequiredProperty("spring.datasource.url");
      try {
         Class.forName(env.getRequiredProperty("spring.datasource.driver-class-name"));
         log.debug("Trying to connect to {}", url);
         Connection connection = DriverManager.getConnection(url,
             env.getRequiredProperty("spring.datasource.username"),
             env.getRequiredProperty("spring.datasource.password"));

         log.info("Connection Established Successful to {} database", connection.getMetaData().getDatabaseProductName());
         connection.close();
         return true;
      } catch (Exception e) {
         log.warn("Cannot connect to database {}. Cause: {}", url, e.getMessage());
         log.trace(e.getMessage(), e);
         return false;
      }
   }
}
