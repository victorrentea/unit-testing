package victor.testing.mocks.fake;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class FeedProcessorFakeTest {

   FileRepoFake repoFake = new FileRepoFake();
   FeedProcessor feedProcessor = new FeedProcessor(repoFake);

   @Test
   public void oneFileWithOneLine() {
      repoFake.addFile("one.txt", "one");
//      when(repoFake.getFileNames()).thenReturn(Arrays.asList("one.txt"));
//      when(repoFake.openFile("one.txt")).thenReturn(Stream.of("one"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      repoFake.addFile("two.txt", "one", "two");
//      when(repoFake.getFileNames()).thenReturn(Arrays.asList("two.txt"));
//      when(repoFake.openFile("two.txt")).thenReturn(Stream.of("one","two"));

      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
      repoFake.addFile("one.txt", "one");
      repoFake.addFile("two.txt", "one", "two");
//      when(repoFake.getFileNames()).thenReturn(Arrays.asList("two.txt", "one.txt"));
//      when(repoFake.openFile("two.txt")).thenReturn(Stream.of("one","two"));
//      when(repoFake.openFile("one.txt")).thenReturn(Stream.of("one"));

      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
      // TODO How to DRY the tests?
   }


}
