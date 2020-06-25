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
public class FeedProcessorWithDummyTest {

   @Autowired
   private FeedProcessor feedProcessor;

//   @Autowired
//   private FileRepoDummy fileRepoDummy;

   @Test
   public void test1() throws IOException {
//      fileRepoDummy.addFileContents("one.txt", "One Line");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }
}
