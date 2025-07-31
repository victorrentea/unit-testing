package victor.testing.mocks.fake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedProcessorTest {

   @InjectMocks
   FeedProcessor feedProcessor;
   @Mock
   FileRepo fileRepoMock;

  @Test
  void oneFileWithOneLine() {
      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

  @Test
  void oneFileWith2Lines() {
      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("two.txt"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

  @Test
  void twoFilesWith3Lines() {
      // TODO

      // TODO How to DRY the tests?
   }


}
