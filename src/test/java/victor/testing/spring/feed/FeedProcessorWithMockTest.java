package victor.testing.spring.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FeedProcessorWithMockTest {

	@Autowired
	private FeedProcessor feedProcessor;
	@MockBean
	private FileRepo fileRepoMock;

	@Test
	public void oneFileWithOneLine() {
		when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt"));
		when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
		assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
	}

	@Test
	public void noLines() {
		when(fileRepoMock.getFileNames()).thenReturn(asList());
		assertThat(feedProcessor.countPendingLines()).isEqualTo(0);
	}

	@Test
	public void oneFileWith2Lines() {
		when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("two.txt"));
		when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one", "two"));
		assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
	}

	@Test
	public void twoFilesWith3Lines() {
		when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt", "two.txt"));
		when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
		when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one", "two"));
		assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
	}

}
