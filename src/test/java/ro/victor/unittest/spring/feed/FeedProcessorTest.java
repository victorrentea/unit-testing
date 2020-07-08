package ro.victor.unittest.spring.feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest // cauta in sus primul pachet care sa contina o @SpringBootApplication
@RunWith(SpringRunner.class)
public class FeedProcessorTest {
   @Autowired
   private FeedProcessor processor;

   @Test
   public void test() throws IOException {
      processor.countPendingLines();
   }
}
