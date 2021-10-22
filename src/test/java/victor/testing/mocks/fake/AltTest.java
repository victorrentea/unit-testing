package victor.testing.mocks.fake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles({"file-repo-fake", "db-mem"})
public class AltTest {

   @Autowired
   FileRepoFake fileRepoFake;
   @Autowired
   FeedProcessor feedProcessor;

   @BeforeEach
   final void before() {
      fileRepoFake.clearData();
   }
   @Test
   public void oneFileWithOneLine() {
      fileRepoFake.addFile("3.txt", "one");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }


}
