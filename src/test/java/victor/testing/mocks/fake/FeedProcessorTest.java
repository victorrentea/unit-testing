package victor.testing.mocks.fake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedProcessorTest {

   @InjectMocks
   FeedProcessor feedProcessor;
   @Mock
   FileRepo repoMock;

   @Test
   public void oneFileWithOneLine() {
      when(repoMock.getFileNames()).thenReturn(Arrays.asList("one.txt"));
      when(repoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      when(repoMock.getFileNames()).thenReturn(Arrays.asList("two.txt"));
      when(repoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
      when(repoMock.getFileNames()).thenReturn(Arrays.asList("two.txt", "one.txt"));
      when(repoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));
      when(repoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
      // TODO How to DRY the tests?
   }


}
