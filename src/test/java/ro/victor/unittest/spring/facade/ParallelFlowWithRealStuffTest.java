package ro.victor.unittest.spring.facade;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(properties = "image.service.url=http://localhost:9987/image")
@Slf4j
@RunWith(SpringRunner.class) // doar pt junit4
public class ParallelFlowWithRealStuffTest {

   @Autowired
   private ParallelFlow flow;

   @Rule
   public WireMockRule wireMockRule = new WireMockRule(9987);

   @Test
   public void fiveInParallel() {

      long t0 = System.currentTimeMillis();
      flow.doIt("ceva");
      long t1 = System.currentTimeMillis();
      Assertions.assertThat(t1-t0).isLessThan(2000);
   }
}
