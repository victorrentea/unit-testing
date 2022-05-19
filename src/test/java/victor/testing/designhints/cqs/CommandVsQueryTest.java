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
      Obj obj = new Obj();
      when(dependency.computeStuff(5)).thenReturn(7);

      target.testedMethod(obj);

      verify(dependency).changeStuff(obj, 5); // why ??
      // ASK:1) Am i violating CQS ? is my function both returning and changing stuff ?
      // or 2) the method I am verifying is an expensive call that I want to make sure it happens once
      assertThat(outputCapture.toString()).isEqualToIgnoringNewLines("Logic with 7");
   }
}