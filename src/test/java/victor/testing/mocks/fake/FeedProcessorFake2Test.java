package victor.testing.mocks.fake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(MockitoExtension.class)

@SpringBootTest
@ActiveProfiles("db-mem")
public class FeedProcessorFake2Test {
   @Autowired
   FileRepoFake fileRepo;
   @Autowired
   FeedProcessor feedProcessor;

   public FeedProcessorFake2Test() {
      System.out.println("WOW! unlike testNG");
   }
   @BeforeEach
   final void before() {
       fileRepo.clearFiles();
   }

   @Test
   public void oneFileWithOneLine() {
      fileRepo.addFile("1.txt", List.of("one"));

      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      fileRepo.addFile("2.txt", List.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
      fileRepo.addFile("3.txt", List.of("one"));
      fileRepo.addFile("4.txt", List.of("one","two"));

      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }


}
