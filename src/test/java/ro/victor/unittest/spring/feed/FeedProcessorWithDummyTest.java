package ro.victor.unittest.spring.feed;
// DONE
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
//@ActiveProfiles("dummy-file-repo")
public class FeedProcessorWithDummyTest {

   @Autowired
   private FeedProcessor feedProcessor;

   @Autowired
   private FileRepoDummy fileRepoDummy;

//   @TestConfiguration
//   public static class InnerConfig {
//      @Bean
//      @Primary
//      public FileRepoDummy fileRepoDummy() {
//         return new FileRepoDummy();
//      }
//   }

   @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
   @Test
   public void test1() throws IOException {
      fileRepoDummy.addFileContents("one.txt", "One Line");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }
   @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
   @Test
   public void test2() throws IOException {
      fileRepoDummy.addFileContents("two.txt", "Two\nLines");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
   @Test
   public void test3() throws IOException {
      fileRepoDummy.addFileContents("one.txt", "One Line");
      fileRepoDummy.addFileContents("two.txt", "Two\nLines");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }
}
