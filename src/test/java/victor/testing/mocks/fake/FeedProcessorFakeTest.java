package victor.testing.mocks.fake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class FileRepoFake implements IFileRepo {
   Map<String, List<String>> fileContents = new HashMap<>();

   @Override
   public Collection<String> getFileNames() {
      return fileContents.keySet();
   }

   @Override
   public Stream<String> openFile(String fileName) {
      return fileContents.get(fileName).stream();
   }

   public void addFile(String fileName, List<String> contents) {
      fileContents.put(fileName, contents);
   }
}

//@ExtendWith(MockitoExtension.class)

public class FeedProcessorFakeTest {

   FileRepoFake fileRepo = new FileRepoFake();
   FeedProcessor feedProcessor = new FeedProcessor(fileRepo);

   public FeedProcessorFakeTest() {
      System.out.println("WOW! unlike testNG");
   }

   @Test
   public void oneFileWithOneLine() {
//      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
      fileRepo.addFile("1.txt", List.of("one"));

      int actual = feedProcessor.countPendingLines();

      assertThat(actual).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      fileRepo.addFile("2.txt", List.of("one","two"));
//      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("two.txt"));
//      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
      fileRepo.addFile("3.txt", List.of("one"));
      fileRepo.addFile("4.txt", List.of("one","two"));
//
//      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt","two.txt"));
//      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
//      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));

      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);

      // TODO How to DRY the tests?
   }


}
