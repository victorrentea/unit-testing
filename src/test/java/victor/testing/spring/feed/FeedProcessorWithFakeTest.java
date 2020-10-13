package victor.testing.spring.feed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("fakeFileRepo")
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) -- nu comiti pe jenkins
public class FeedProcessorWithFakeTest {

   @Autowired
   private FeedProcessor feedProcessor;
   @Autowired
   private FileRepoInMemoryForTests fileRepoFake;

   @BeforeEach
   public void initialize() {
       fileRepoFake.clearFiles();

   }

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
      fileRepoFake.addFile("one.txt", "one");
      fileRepoFake.addFile("two.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }
   @Test
   public void twoFilesWith3Lines_1Commented() {
      fileRepoFake.addFile("onecomment.txt", "#comment");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(0);
   }
}
