package ro.victor.unittest.spring.feed;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FeedProcessorMockTest {
   @Autowired
   private FeedProcessor processor;

   @MockBean
   private IFileRepo mockRepo;

   // mockurile se fac .reset() automat intre @Test

   @Test
   public void oneXLine() throws IOException {
      when(mockRepo.getFileNames()).thenReturn(Collections.singleton("one.txt"));
      when(mockRepo.openFile("one.txt")).thenReturn(IOUtils.toInputStream("one line"));
      assertThat(processor.countPendingLines()).isEqualTo(1);
   }
   @Test
   public void testYTwo() throws IOException {
      when(mockRepo.getFileNames()).thenReturn(new HashSet<>(Arrays.asList("one.txt", "two.txt")));
      when(mockRepo.openFile("one.txt")).thenReturn(IOUtils.toInputStream("one line"));
      when(mockRepo.openFile("two.txt")).thenReturn(IOUtils.toInputStream("one line"));
      assertThat(processor.countPendingLines()).isEqualTo(2);
   }
}
