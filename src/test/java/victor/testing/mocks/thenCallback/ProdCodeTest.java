package victor.testing.mocks.thenCallback;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdCodeTest {
   @Mock
   AnotherClassInYourCode anotherClass;
   @InjectMocks
   ProdCode prodCode;

   @Test
   void methodToTest() {
      doAnswer(call -> {
         List<String> list = call.getArgument(0);
         list.add("a");
         list.add("b");
         return null;
      }).when(anotherClass).collectMore(anyList());

      String result = prodCode.methodToTest();

      assertThat(result).isEqualTo("a\nb");
   }

}