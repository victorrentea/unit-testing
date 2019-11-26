package ro.victor.unittest.db;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import ro.victor.unittest.db.prod.NotificationRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AssertDatabaseContentsTest {

	@Autowired
	private NotificationRepo notificationRepo;

	@ExpectedDatabase(value = "AssertDatabaseContents-expected.xml", assertionMode = NON_STRICT)
	@Test
	public void notificationTextIsCorrectlyInserted() {
		notificationRepo.insertNotification("aaa");
	}

}
