package ro.victor.unittest.spring.feed;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // cauta in sus primul pachet care sa contina o @SpringBootApplication
@RunWith(SpringRunner.class)
@ActiveProfiles("test-real-fs") // adica doar acest test vrea sa mearga pe folder real, nu in mem
public class FeedProcessorTest {
   @Autowired
   private FeedProcessor processor;

   @Autowired
   private FileRepoDummy dummyRepo;

   @Test
   public void test() throws IOException {
      dummyRepo.addTestFile("one.txt", "one line");
      assertThat(processor.countPendingLines()).isEqualTo(1);
   }
}
