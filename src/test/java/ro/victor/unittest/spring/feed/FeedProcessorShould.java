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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedProcessorShould {
   @Autowired
   private FeedProcessor processor;

   @MockBean
   private FileRepo fileRepoMock;

   @Test
   public void test() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(new HashSet<>(asList("one.txt")));
      when(fileRepoMock.openFile("one.txt")).thenReturn(IOUtils.toInputStream("lineOne"));
      int actualLines = processor.countPendingLines();
      assertEquals(1, actualLines);
   }
}
