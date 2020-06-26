package ro.victor.unittest.spring.feed;
// DONE
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FeedProcessorWithMockTest {

   @Autowired
   private FeedProcessor feedProcessor;
   @MockBean
   private IFileRepo fileRepoMock;

   @Test
   public void test1() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(Collections.singleton("one.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(IOUtils.toInputStream("one line"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }
}
