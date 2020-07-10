package ro.victor.unittest.spring.feed;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest()
@RunWith(SpringRunner.class)
public class FeedProcessorWithMockBeanTest {
   @Autowired
   private FeedProcessor processor;

   @MockBean
   private FileRepo repoMock;

   @Test
   public void oneFileWithOneLine() throws IOException {
      when(repoMock.getFileNames()).thenReturn(new HashSet<>(asList("one.txt")));
      when(repoMock.openFile("one.txt")).thenReturn(toInputStream("oneLine"));
      int count = processor.countPendingLines();
      assertEquals(1, count);
   }
   @Test
   public void oneFileWithTwoLines() throws IOException {
      when(repoMock.getFileNames()).thenReturn(new HashSet<>(asList("one.txt")));
      when(repoMock.openFile("one.txt")).thenReturn(toInputStream("oneLine\ntwo"));
      int count = processor.countPendingLines();
      assertEquals(2, count);
   }
   @Test
   public void twoFiles() throws IOException {
      when(repoMock.getFileNames()).thenReturn(new HashSet<>(asList("one.txt", "two.txt")));
      when(repoMock.openFile("one.txt")).thenReturn(toInputStream("oneLine"));
      when(repoMock.openFile("two.txt")).thenReturn(toInputStream("oneLine2"));
      int count = processor.countPendingLines();
      assertEquals(2, count);
   }
}
