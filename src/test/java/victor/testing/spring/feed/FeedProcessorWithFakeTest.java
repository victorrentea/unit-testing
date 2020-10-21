package victor.testing.spring.feed;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dummyFileRepo")
public class FeedProcessorWithFakeTest {

   @Autowired
   private FeedProcessor feedProcessor;
   @Autowired
   private FileRepoForTests fileRepo;

   @Test
   public void oneFileWithOneLine() {
      fileRepo.addTestFile("a.txt", "one");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      fileRepo.addTestFile("a.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3LinesInTotal() {
      fileRepo.addTestFile("a.txt", "one");
      fileRepo.addTestFile("b.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }

   @Test
   public void twoFilesWith3Lines() {
      // TODO

      // TODO How to DRY the tests?
   }


}
