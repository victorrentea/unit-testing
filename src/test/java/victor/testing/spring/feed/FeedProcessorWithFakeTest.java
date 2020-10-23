package victor.testing.spring.feed;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)

@ActiveProfiles("realRepo")
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class FeedProcessorWithFakeTest {

	@Autowired
	private FeedProcessor feedProcessor;
	@Autowired
	private FileRepoFakeForTests fileRepoFake;

	@Before
	public void initialize() {
		fileRepoFake.clearFiles();
	}
	@Test
	public void oneFileWithOneLine() {
		fileRepoFake.addFile("one.txt", "one");
		assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
	}

	@Test
	public void noLines() {
		assertThat(feedProcessor.countPendingLines()).isEqualTo(0);
	}

	@Test
	public void oneFileWith2Lines() {
		fileRepoFake.addFile("two.txt", "one", "two");
		assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
	}

	@Test
	public void twoFilesWith3Lines() {
		fileRepoFake.addFile("one.txt", "one");
		fileRepoFake.addFile("two.txt", "one", "two");
		assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
	}

}
