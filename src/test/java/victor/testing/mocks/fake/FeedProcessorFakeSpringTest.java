package victor.testing.mocks.fake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // only use as debug too. NEVER comit on Git.!!
public class FeedProcessorFakeSpringTest {
//   FileRepoFake repoFake = new FileRepoFake();
   @Autowired
   FeedProcessor feedProcessor;
   @Autowired
   FileRepoFake repoFake;

   @BeforeEach
   final void cleanup() {
       repoFake.clear();
       // data in DB,
      // cassandra, caches, mongo, files on disk
   }

   @Test
   public void oneFileWithOneLine() {
      repoFake.addFile("one.txt", "one");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      repoFake.addFile("two.txt", "one", "two");

      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
      repoFake.addFile("one.txt", "one");
      repoFake.addFile("two.txt", "one", "two");

      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
      // TODO How to DRY the tests?
   }



}
