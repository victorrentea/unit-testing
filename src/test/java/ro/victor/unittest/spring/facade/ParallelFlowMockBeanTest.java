package ro.victor.unittest.spring.facade;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class) // doar pt junit4
public class ParallelFlowMockBeanTest {

   @Autowired
   private ParallelFlow flow;

   @MockBean
   private OtherClient clientMock;


   @Test
   public void fiveInParallel() {
      Mockito.when(clientMock.callSync("ceva"))
          .thenAnswer(new AnswersWithDelay(1000,
              p -> {
             log.debug("Din ce thread");
                 return "CAT";
              }));

      long t0 = System.currentTimeMillis();
      flow.doIt("ceva");
      long t1 = System.currentTimeMillis();
      Assertions.assertThat(t1-t0).isLessThan(500);
   }
}
