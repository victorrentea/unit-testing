package victor.testing.designhints.cqs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.tools.CaptureSystemOutput;
import victor.testing.tools.CaptureSystemOutput.OutputCapture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandVsQueryTest {
   @Mock Dependency dependency;
   @InjectMocks
   Target target;

   @Test
   @CaptureSystemOutput
   void test(OutputCapture outputCapture) {
      Obj obj = new Obj(0);
      when(dependency.derive(obj, 5))
              .thenReturn(new Obj(5));
      when(dependency.query( 5)).thenReturn(10);

      target.testedMethod(obj);

//      verify(dependency).mutateAndReturn(obj, 5);
      ; // why ??
//      InOrder inOrder = inOrder(dependency);
//      inOrder.verify(dependency).mutateAndReturn(obj, 5);
//      inOrder.verify(dependency).query(anyInt());

      assertThat(outputCapture.toString()).isEqualToIgnoringNewLines("Logic with 10");
   }
}