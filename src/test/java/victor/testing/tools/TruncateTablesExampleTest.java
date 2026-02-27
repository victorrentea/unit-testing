package victor.testing.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example test demonstrating the usage of @TruncateTables extension.
 * <p>
 * This test shows how the extension automatically cleans up database tables
 * before and after each test method, ensuring test isolation.
 */
@SpringBootTest
@ActiveProfiles("test")
@TruncateTables({"product", "supplier"})
class TruncateTablesExampleTest {

  @Autowired
  private DataSource dataSource;

  @Test
  void tablesAreEmptyBeforeTest() throws Exception {
    // The @TruncateTables extension ensures these tables are empty before each test
    assertTableIsEmpty("product");
    assertTableIsEmpty("supplier");
  }

  @Test
  void tablesAreCleanedBetweenTests() throws Exception {
    // Even if the previous test inserted data, this test starts with clean tables
    assertTableIsEmpty("product");
    assertTableIsEmpty("supplier");

    // Insert some test data
    insertTestData();

    // Verify data was inserted
    assertTableIsNotEmpty("supplier");
  }

  @Test
  void anotherTestAlsoStartsWithCleanTables() throws Exception {
    // The extension cleaned up after the previous test
    assertTableIsEmpty("product");
    assertTableIsEmpty("supplier");
  }

  private void assertTableIsEmpty(String tableName) throws Exception {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
      rs.next();
      int count = rs.getInt(1);
      assertThat(count)
          .as("Table " + tableName + " should be empty")
          .isEqualTo(0);
    }
  }

  private void assertTableIsNotEmpty(String tableName) throws Exception {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
      rs.next();
      int count = rs.getInt(1);
      assertThat(count)
          .as("Table " + tableName + " should not be empty")
          .isGreaterThan(0);
    }
  }

  private void insertTestData() throws Exception {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
      stmt.executeUpdate("INSERT INTO supplier(id, name, active) VALUES (1, 'Test Supplier', true)");
    }
  }
}

