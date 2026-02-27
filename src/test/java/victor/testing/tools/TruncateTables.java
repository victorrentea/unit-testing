package victor.testing.tools;

import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.lang.annotation.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JUnit extension that truncates specified database tables before and after each test.
 * <p>
 * Usage:
 * <pre>
 * {@code
 * @TruncateTables({"product", "supplier"})
 * class MyTest {
 *   // tests...
 * }
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(TruncateTables.Extension.class)
public @interface TruncateTables {

  /**
   * Names of the tables to truncate before and after each test.
   */
  String[] value();

  class Extension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
      truncateTables(context);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
      truncateTables(context);
    }

    private void truncateTables(ExtensionContext context) throws SQLException {
      TruncateTables annotation = context.getRequiredTestClass().getAnnotation(TruncateTables.class);
      if (annotation == null) {
        return;
      }

      String[] tables = annotation.value();
      if (tables.length == 0) {
        return;
      }

      DataSource dataSource = getDataSource(context);

      try (Connection connection = dataSource.getConnection();
           Statement statement = connection.createStatement()) {

        // Disable foreign key checks (works for H2, PostgreSQL, MySQL)
        try {
          //noinspection SqlDialectInspection,SqlNoDataSourceInspection
          statement.execute("SET REFERENTIAL_INTEGRITY FALSE"); // H2
        } catch (SQLException e) {
          // Try alternative syntax for other databases
          try {
            //noinspection SqlDialectInspection,SqlNoDataSourceInspection
            statement.execute("SET CONSTRAINTS ALL DEFERRED"); // PostgreSQL
          } catch (SQLException e2) {
            // Ignore if not supported
          }
        }

        // Truncate all specified tables
        for (String table : tables) {
          try {
            //noinspection SqlDialectInspection,SqlNoDataSourceInspection
            statement.execute("TRUNCATE TABLE " + table);
          } catch (SQLException e) {
            // If TRUNCATE fails, try DELETE as fallback
            //noinspection SqlDialectInspection,SqlNoDataSourceInspection,SqlWithoutWhere
            statement.execute("DELETE FROM " + table);
          }

          // Try to reset auto-increment/sequence for the table
          try {
            //noinspection SqlDialectInspection,SqlNoDataSourceInspection
            statement.execute("ALTER TABLE " + table + " ALTER COLUMN id RESTART WITH 1"); // H2
          } catch (SQLException e) {
            // Ignore if not supported or no id column
          }
        }

        // Re-enable foreign key checks
        try {
          //noinspection SqlDialectInspection,SqlNoDataSourceInspection
          statement.execute("SET REFERENTIAL_INTEGRITY TRUE"); // H2
        } catch (SQLException e) {
          try {
            //noinspection SqlDialectInspection,SqlNoDataSourceInspection
            statement.execute("SET CONSTRAINTS ALL IMMEDIATE"); // PostgreSQL
          } catch (SQLException e2) {
            // Ignore if not supported
          }
        }
      }
    }

    private DataSource getDataSource(ExtensionContext context) {
      return SpringExtension.getApplicationContext(context)
          .getBean(DataSource.class);
    }
  }
}

