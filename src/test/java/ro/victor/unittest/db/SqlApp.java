package ro.victor.unittest.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.jdbc.Sql;

@SpringBootApplication
public class SqlApp {
   public static void main(String[] args) {
      SpringApplication.run(SqlApp.class, args);
   }
}
