package ro.victor.unittest.spring.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.junit.Assert.assertEquals;

@SpringBootTest()
@RunWith(SpringRunner.class)
@ActiveProfiles("dummyFileRepo")
public class FeedProcessorWithDummyImplTest {
   @Autowired
   private FeedProcessor processor;

   @Autowired
   private FileRepoFromMemoryForTests dummyRepo;

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
