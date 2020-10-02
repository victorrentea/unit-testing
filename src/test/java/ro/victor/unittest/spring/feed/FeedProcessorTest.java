package ro.victor.unittest.spring.feed;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedProcessorTest {
   @MockBean
   private FileRepo fileRepoMock;

   @Autowired
   private FeedProcessor feedProcessor;

   @Test
   public void noFiles() throws IOException {
      int count = feedProcessor.countPendingLines();
      assertEquals(0, count);
   }
   @Test
//   public void counts1Line_when1FileWithOneLine() throws IOException {
   public void oneFileWithOneLine() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(new StringReader("line1"));
      int lineCount = feedProcessor.countPendingLines();
      assertEquals(1, lineCount);
   }
   @Test
   public void twoFileWithOneLineEach() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt", "two.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(new StringReader("lineA"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(new StringReader("lineB"));
      int lineCount = feedProcessor.countPendingLines();
      assertEquals(2, lineCount);
   }
   @Test
   public void file1OneLine_file2TwoLines() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt", "two.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(new StringReader("lineA"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(new StringReader("lineB1\nLineB2"));
      int lineCount = feedProcessor.countPendingLines();
      assertEquals(3, lineCount);
   }
}