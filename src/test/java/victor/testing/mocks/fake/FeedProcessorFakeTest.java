package victor.testing.mocks.fake;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles({"file-repo-fake", "db-mem"})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // iNTERSZIS sa comiti pe jenkins (alex de reject)
public class FeedProcessorFakeTest {

   @Autowired
   FileRepoFake fileRepoFake;
   @Autowired
   FeedProcessor feedProcessor;

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
