package ro.victor.unittest.spring.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FeedProcessorTest {

   @Autowired
   private FeedProcessor feedProcessor;

   @Test
   public void test1() throws IOException {
      //if one file containing 1 line
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }
}
