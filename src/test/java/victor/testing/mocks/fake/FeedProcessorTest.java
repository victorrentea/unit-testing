package victor.testing.mocks.fake;

import org.junit.jupiter.api.Test;
import victor.testing.spring.feed.FeedProcessor;
import victor.testing.spring.feed.FileRepoFake;

import static org.assertj.core.api.Assertions.assertThat;

public class FeedProcessorTest {

   FileRepoFake fileRepoFake = new FileRepoFake();
   FeedProcessor feedProcessor = new FeedProcessor(fileRepoFake);


   @Test
   public void oneFileWithOneLine() {
      fileRepoFake.addFile("one.txt", "one");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      fileRepoFake.addFile("one.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
      fileRepoFake.addFile("one.txt", "one");
      fileRepoFake.addFile("two.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }


}
