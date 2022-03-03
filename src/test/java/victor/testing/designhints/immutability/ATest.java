package victor.testing.designhints.immutability;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ATest {
   @InjectMocks A a;
   @Mock ObjRepo objRepo;
   @Mock B b;

   @Test
   void caller() {
      when(objRepo.findById(1L)).thenReturn(new Obj());
//      doAnswer(invocation -> {
//         Obj obj = (Obj) invocation.getArguments()[0];
//         return obj.setX("stubbed");
//      }).when(b).method(any());
      when(b.method(any())).thenReturn("stubbed");

      String result = a.caller(1L);

      assertThat(result).isEqualTo("stubbed");
   }
}