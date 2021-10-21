package victor.testing.mocks.fake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedProcessorTest {

   @InjectMocks
   FeedProcessor feedProcessor;
   @Mock
   FileRepo fileRepoMock;

   @Test
   public void oneFileWithOneLine() {
      when(fileRepoMock.getFileNames()).thenReturn(List.of("one.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      when(fileRepoMock.getFileNames()).thenReturn(List.of("two.txt"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
      when(fileRepoMock.getFileNames()).thenReturn(List.of("one.txt", "two.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("line1"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("line2", "line3"));

      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);

      // TODO How to DRY the tests?
   }


}
