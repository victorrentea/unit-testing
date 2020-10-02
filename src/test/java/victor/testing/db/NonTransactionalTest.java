package victor.testing.db;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import victor.testing.db.prod.NotificationRepo;
import victor.testing.db.prod.ReportingRepo;

@SpringBootTest
@ActiveProfiles("realdb") // result is preserved in a read DB (stored on disk)
@RunWith(SpringRunner.class)
// SOLUTION (
@WithCommonReferenceData
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD,
	statements = {
		"DELETE FROM ORDERS",
		"DELETE FROM USERS"
})
// SOLUTION )
public class NonTransactionalTest {

	@Autowired
	private NotificationRepo notificationRepo;

	@Autowired
	private ReportingRepo reportingRepo;
	
	@Autowired
	private JdbcTemplate jdbc;


	@Test
	public void countTotalUsers() {
		Integer count = jdbc.queryForObject("SELECT count(1) FROM users", Integer.class);
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void testUserExistsInDB() {
		Integer count = jdbc.queryForObject("SELECT count(1) FROM users WHERE username='test'", Integer.class);
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void notificationTextIsCorrectlyPersisted() {
		notificationRepo.insertNotification("a");
		assertEquals(singletonList("a"), reportingRepo.getAllNotifications());
	}

	// !! NOTE: Placing .sql files next to JUnit tests is possible only if in pom.xml you have <testResources> src/test/java
	@Sql("/common-reference-data.sql")// SOLUTION
	@Sql// SOLUTION
	// @CleanupSql // TODO
	@Test
	public void orderExistsByReference() {
		Integer count = jdbc.queryForObject("SELECT count(1) FROM orders WHERE reference='ref'", Integer.class);
		assertThat(count).isEqualTo(1);
	}

}
