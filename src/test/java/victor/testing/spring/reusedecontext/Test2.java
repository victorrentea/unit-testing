package victor.testing.spring.reusedecontext;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import victor.testing.spring.infra.SafetyClient;

public class Test2 extends  MockExternalsSpringTestBase{

   @Autowired
   ServiceX serviceX;

   @Test
   void test() {

   }
}



@RequiredArgsConstructor
@Service
class ServiceX {
   private final  AltClient client;
}

@Component
class AltClient {

}

@SpringBootTest
abstract class MockExternalsSpringTestBase {
   @MockBean
   AltClient altClient;
   @MockBean
   SafetyClient safetyClient;
}