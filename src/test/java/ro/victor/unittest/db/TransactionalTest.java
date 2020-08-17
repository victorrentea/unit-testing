package ro.victor.unittest.db;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ro.victor.unittest.db.prod.NotificationRepo;
import ro.victor.unittest.db.prod.ReportingRepo;

@Ignore("Investigate on-demand")
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional // SOLUTION
@WithCommonReferenceData // SOLUTION
public class TransactionalTest {

	@Autowired
	private NotificationRepo notificationRepo;

	@Autowired
	private ReportingRepo reportingRepo;

	@Autowired
	private JdbcTemplate jdbc;

	@Sql// SOLUTION
	@Test
	public void countTotalUsers() {
		Integer count = jdbc.queryForObject("SELECT count(1) FROM users", Integer.class);
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void testUserIsInDB() {
		Integer count = jdbc.queryForObject("SELECT count(1) FROM users WHERE username='test'", Integer.class);
		assertThat(count).isEqualTo(1);
	}

	@Test
	public void notificationTextIsCorrectlyPersisted() {
		notificationRepo.insertNotification("a");
		assertThat(reportingRepo.getAllNotifications()).containsExactlyInAnyOrder("a");
	}

	@Sql("/common-reference-data.sql")// SOLUTION
	@Sql// SOLUTION
	@Test
	public void orderExistsByReference() {
		Integer count = jdbc.queryForObject("SELECT count(1) FROM orders WHERE reference='ref'", Integer.class);
		assertThat(count).isEqualTo(1);
	}

}
