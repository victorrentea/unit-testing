package ro.victor.unittest.spring.feed;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FeedProcessorWithDummyShould {
   @Autowired
   private FeedProcessor processor;

   @Autowired
   // In mod normal preferi @Mock mockito fara Spring. (de ~20-100x mai rapid)
   // Dar e necesar @MockBean cand:
   // A) sunt foarte multe clase implicate (5+) - sa le legi manual cu new e nasol. sa nu-ti pse care de care se leaga
   // B) cand ai nevoie de spring printre metodele tale. Ex: @Cacheable si @Trasactional
   private FileRepoDummyForTests fileRepoDummy;

   @Test
   public void test() throws IOException {
      fileRepoDummy.addDummyFile("one.txt", "oneLine");
//      when(fileRepoDummy.getFileNames()).thenReturn(new HashSet<>(asList("one.txt")));
//      when(fileRepoDummy.openFile("one.txt")).thenReturn(IOUtils.toInputStream("lineOne"));
      int actualLines = processor.countPendingLines();
      assertEquals(1, actualLines);
   }
//   @Test
//   public void test2() throws IOException {
//         when(fileRepoDummy.getFileNames()).thenReturn(new HashSet<>(asList("two.txt")));
//         when(fileRepoDummy.openFile("two.txt")).thenReturn(IOUtils.toInputStream("lineOne\nlineTwo"));
//      int actualLines = processor.countPendingLines();
//      assertEquals(2, actualLines);
//   }
//   @Test
//   public void test3() throws IOException {
//      when(fileRepoDummy.getFileNames()).thenReturn(new HashSet<>(asList("one.txt","two.txt")));
//      when(fileRepoDummy.openFile("one.txt")).thenReturn(IOUtils.toInputStream("lineOne"));
//      when(fileRepoDummy.openFile("two.txt")).thenReturn(IOUtils.toInputStream("lineOne\nlineTwo"));
//      int actualLines = processor.countPendingLines();
//      assertEquals(3, actualLines);
//   }
}
