package victor.testing.spring.feed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dummyFileRepo")
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FeedProcessorWithFakeTest {

   @Autowired
   private FeedProcessor feedProcessor;
   @Autowired
   private FileRepoForTests fileRepo;

   @BeforeEach
   public void initialize() {
      fileRepo.clearFiles();
   }

   @Test
   public void oneFileWithOneLine() {
//      applicationContext.getBean(FileRepoForTests.class)
      fileRepo.addTestFile("a.txt", "one");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      fileRepo.addTestFile("b.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3LinesInTotal() {
      fileRepo.addTestFile("c.txt", "one");
      fileRepo.addTestFile("d.txt", "one", "two");
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }

   @Test
   public void twoFilesWith3Lines() {
      // TODO

      // TODO How to DRY the tests?
   }


}
