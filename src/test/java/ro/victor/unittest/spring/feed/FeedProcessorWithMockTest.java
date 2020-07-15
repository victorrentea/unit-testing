package ro.victor.unittest.spring.feed;
// DONE

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FeedProcessorWithMockTest {

   @Autowired
   private FeedProcessor feedProcessor;
   @MockBean
   private IFileRepo fileRepoMock;

   @Test
   public void oneFileWithOneLine() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(asList("one.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(new StringReader("one line"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(asList("two.txt"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(new StringReader("one\ntwo"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt", "two.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(new StringReader("one line"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(new StringReader("one\ntwo"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }
}
