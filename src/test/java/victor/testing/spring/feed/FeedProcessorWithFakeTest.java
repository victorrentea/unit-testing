package victor.testing.spring.feed;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class FeedProcessorWithFakeTest {

   @Autowired
   private FeedProcessor feedProcessor;
   @Autowired
   private FileRepoForTests fileRepo;

   @Test
   public void oneFileWithOneLine() {
      fileRepo.addFile("a.txt", "one");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      fileRepo.addFile("a.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3LinesInTotal() {
      fileRepo.addFile("a.txt", "one");
      fileRepo.addFile("b.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }

   @Test
   public void twoFilesWith3Lines() {
      // TODO

      // TODO How to DRY the tests?
   }


}
