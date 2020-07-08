package ro.victor.unittest.spring.feed;

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
public class FeedProcessor2Test {

   // 1: pot cele doua clase de pe eran sa foloseasca acelasi
   // context de spring
   // daca ar face-o ar gasi aici implem dummy de repo, nu cea reala


   @Autowired
   private FeedProcessor processor;


   @Test
   public void test() throws IOException {
      assertThat(processor.countPendingLines()).isEqualTo(0);
   }
}
