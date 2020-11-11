package victor.testing.db;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.springtestdbunit.annotation.ExpectedDatabase;

import victor.testing.db.prod.NotificationRepo;


//@ClearCache
//@Dataset(value = "dataset/ConfigOptionsDaoImplTest_dataset.xml", flatXml = true)
//@ContextConfiguration("classpath:common-dao-context.xml")
//@Transactional
//@TransactionConfiguration
//public class ConfigOptionsDaoImplTest extends AbstractTestsAssertions {
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
