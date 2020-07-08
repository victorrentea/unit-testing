package ro.victor.unittest.spring.feed;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // cauta in sus primul pachet care sa contina o @SpringBootApplication
@RunWith(SpringRunner.class)
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FeedProcessorTest {
   @Autowired
   private FeedProcessor processor;

   @Autowired
   private FileRepoDummy dummyRepo;

//   @After
//   public void mamaMiaZisSaCuratDupaMine() {
//      dummyRepo.cleanFiles();
//   }
   @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
   @Test
   public void oneXLine() throws IOException {
      dummyRepo.addTestFile("one.txt", "one line");
      assertThat(processor.countPendingLines()).isEqualTo(1);
      // 2: ce tre sa faci musai aici<<<<<<<<<<
   }
   @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
   @Test
   public void testYTwo() throws IOException {
      dummyRepo.addTestFile("one.txt", "one line");
      dummyRepo.addTestFile("two.txt", "one line");
      assertThat(processor.countPendingLines()).isEqualTo(2);
   }
   @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
   @Test
   public void testZTwoFilesWithThreeLines() throws IOException {
      dummyRepo.addTestFile("one.txt", "one line");
      dummyRepo.addTestFile("two.txt", "two\nlines");
      assertThat(processor.countPendingLines()).isEqualTo(3);
   }
}
