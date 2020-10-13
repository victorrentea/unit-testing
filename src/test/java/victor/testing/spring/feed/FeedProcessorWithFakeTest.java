package victor.testing.spring.feed;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FeedProcessorWithFakeTest {

   @Autowired
   private FeedProcessor feedProcessor;
   @Autowired
   private FileRepoInMemoryForTests fileRepoFake;


   @Test
   public void oneFileWithOneLine() {
//      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt"));
//      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));

      fileRepoFake.addFile("one.txt", "one");

      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

//   @Test
//   public void oneFileWith2Lines() {
//      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("two.txt"));
//      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));
//      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
//   }
//
//   @Test
//   public void twoFilesWith3Lines() {
//      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt", "two.txt"));
//      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
//      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","two")); // << sau daca observi o legatura intre cum mockuiesti metodele unui obiect
////      when(fileRepoMock.openFile("two.txt")).thenAnswer(call -> LocalDateTime.now());  << daca asta
//      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
//   }
//   @Test
//   public void twoFilesWith3Lines_1Commented() {
//      // @MockBean chiar daca contextul in care sunt adaugate supravietuieste intre @Teste,
//      // memoria @MockBean se .reset() intre @Teste
//      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt", "two.txt"));
//      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
//      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","#two"));
//      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
//   }

   // TODO IMAGINE EXTRA DEPENDENCY
}
