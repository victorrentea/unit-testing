package ro.victor.unittest.spring.feed;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@SpringBootTest()
@RunWith(SpringRunner.class)
@ActiveProfiles("dummyFileRepo")
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) // te rupe la timp
public class FeedProcessorWithDummyImpl2Test {
   @Autowired
   private FeedProcessor processor;

   @Autowired
   private FileRepoFromMemoryForTests dummyRepo;
   @Before
   public void initialize() {
      dummyRepo.clearFiles();
   }

   //@DirtiesContext(methodMode = MethodMode.AFTER_METHOD) // = +20-90 sec pierdute dar e safe
   @Test
   public void oneFileWithOneLine() throws IOException {
      dummyRepo.addFile("one.txt", "oneLine");
      int count = processor.countPendingLines();
      assertEquals(1, count);
   }

   @Test
   public void oneFileWithTwoLines() throws IOException {
      dummyRepo.addFile("one.txt", "oneLine\ntwo");
      int count = processor.countPendingLines();
      assertEquals(2, count);
   }

   @Test
   public void twoFiles() throws IOException {
      dummyRepo.addFile("one.txt", "oneLine");
      dummyRepo.addFile("two.txt", "oneLine2");
      int count = processor.countPendingLines();
      assertEquals(2, count);
   }
}
