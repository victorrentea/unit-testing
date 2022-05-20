package victor.testing.mocks.fake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)

@SpringBootTest
@ActiveProfiles({"db-mem"/*,"andThis"*/})
public class FeedProcessorFakeTest {
   @Autowired
   FileRepoFake fileRepo;
   @Autowired
   FeedProcessor feedProcessor;

   public FeedProcessorFakeTest() {
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
