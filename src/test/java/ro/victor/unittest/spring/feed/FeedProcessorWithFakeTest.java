package ro.victor.unittest.spring.feed;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
//@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) -- pt debugging! nu pui pe SVN asaceva, ca enervezi colegii:
// tocmai ai adaugat 1 min la build time pe Jenkins!
public class FeedProcessorWithFakeTest {
   @Autowired
   private FileRepoFakeForTests fileRepoFake;

   @Autowired
   private FeedProcessor feedProcessor;

   @Before
   public void initialize() {
      fileRepoFake.clearFiles();
   }
   @Test
   public void noFiles() throws IOException {
      int count = feedProcessor.countPendingLines();
      assertEquals(0, count);
   }
   @Test
   public void oneFileWithOneLine() throws IOException {
      fileRepoFake.addFakeFile("one.txt", "line1");
      int lineCount = feedProcessor.countPendingLines();
      assertEquals(1, lineCount);
   }
   @Test
   public void twoFileWithOneLineEach() throws IOException {
      fileRepoFake.addFakeFile("one.txt", "lineA");
      fileRepoFake.addFakeFile("two.txt", "lineB");
      int lineCount = feedProcessor.countPendingLines();
      assertEquals(2, lineCount);
   }
   @Test
   public void file1OneLine_file2TwoLines() throws IOException {
      fileRepoFake.addFakeFile("one.txt", "lineA");
      fileRepoFake.addFakeFile("two.txt", "lineB1\nlineB2");
      int lineCount = feedProcessor.countPendingLines();
      assertEquals(3, lineCount);
   }
}