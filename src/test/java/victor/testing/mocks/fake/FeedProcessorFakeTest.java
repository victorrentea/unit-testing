package victor.testing.mocks.fake;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("file-repo-fake")
public class FeedProcessorFakeTest {

   FileRepoFake fileRepoFake = new FileRepoFake();
   FeedProcessor feedProcessor = new FeedProcessor(fileRepoFake);

   @Test
   public void oneFileWithOneLine() {
      fileRepoFake.addFile("one.txt", "one");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      fileRepoFake.addFile("two.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
      fileRepoFake.addFile("one.txt", "line1");
      fileRepoFake.addFile("two.txt", "line2", "line3");

      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }


}
