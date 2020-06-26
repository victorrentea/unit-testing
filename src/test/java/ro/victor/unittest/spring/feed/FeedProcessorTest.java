package ro.victor.unittest.spring.feed;

import org.junit.Before;
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

   @Autowired
   private FileRepoForTest repoForTest;
   @Before
   public void initialize() {
      repoForTest.reset();
   }
   @Test
   public void test1() throws IOException {
      //if one file containing 1 line
      repoForTest.addFile("one.txt", "one line");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }
   @Test
   public void test2() throws IOException {
      //if one file containing 1 line
      repoForTest.addFile("two.txt", "two\nline");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }
   @Test
   public void test3() throws IOException {
      //if one file containing 1 line
      repoForTest.addFile("one.txt", "one line");
      repoForTest.addFile("two.txt", "two\nline");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }
}
