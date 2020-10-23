package victor.testing.spring;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import victor.testing.spring.feed.FeedScanner;
import victor.testing.spring.feed.FileRepo;

@Configuration
@Profile("!realRepo")
public class MockConfig {

	@MockBean
	private FileRepo fileRepoMock;
	@MockBean
	private FeedScanner feedScanner;
}
