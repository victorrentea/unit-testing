package victor.testing.mocks.fake;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class FeedProcessorFakeTest {

   FileRepoFake fileRepoFake = new FileRepoFake();
   FeedProcessor feedProcessor = new FeedProcessor(fileRepoFake);

   @Test
   public void oneFileWithOneLine() {
      fileRepoFake.addTestFile("one.txt", "one");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      fileRepoFake.addTestFile("two.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
      fileRepoFake.addTestFile("one.txt", "one");
      fileRepoFake.addTestFile("two.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }


}
