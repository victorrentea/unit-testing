package ro.victor.unittest.spring.feed;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FeedProcessorMockTest {
   @Autowired
   private FeedProcessor feedProcessor;
   @MockBean
   private IFileRepo mockRepo;

   @Test
   public void test1() throws IOException {
      //if one file containing 1 line
      when(mockRepo.getFileNames()).thenReturn(Collections.singleton("one.txt"));
      when(mockRepo.openFile("one.txt")).thenReturn(IOUtils.toInputStream("one line"));

      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

}
