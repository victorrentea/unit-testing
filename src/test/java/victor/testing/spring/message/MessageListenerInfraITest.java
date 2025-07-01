package victor.testing.spring.message;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import victor.testing.spring.IntegrationTest;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static victor.testing.spring.message.MessageListener.SUPPLIER_CREATED_EVENT;

//@Disabled("slows down tests due to @SpyBean")
public class MessageListenerInfraITest extends IntegrationTest {
  @Autowired
  SqsTemplate sqsTemplate;
  @SpyBean // the real bean is decorated by a mock proxy that can record invocations
  MessageListener messageListener;
  @Value("${supplier.created.event}")
  String queueName;

  @Test
  void listenerIsCalled_whenMessageIsSentVia() {
    sqsTemplate.send(queueName, "supplier");

    verify(messageListener, timeout(3000))
        .onMessage("supplier");
  }
}
