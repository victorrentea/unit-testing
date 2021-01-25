package victor.testing.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("insertDummyData")
@RequiredArgsConstructor
public class DummyDataCreator implements CommandLineRunner {
   private final JdbcTemplate jdbcTemplate;

   @Override
   public void run(String... args) throws Exception {
      // existing shared database
      jdbcTemplate.update("INSERT INTO supplier(ID, NAME, ACTIVE) VALUES (1, 'Dummy', 1)");
      jdbcTemplate.update("INSERT INTO PRODUCT(ID) VALUES (-1)");
      jdbcTemplate.update("INSERT INTO PRODUCT(ID,name) VALUES (-2,'tree')");

      log.info("Inserted dummy data");

   }
}
