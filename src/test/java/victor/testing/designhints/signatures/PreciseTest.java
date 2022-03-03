package victor.testing.designhints.signatures;

import org.junit.jupiter.api.Test;
import victor.testing.tools.CaptureSystemOutput;
import victor.testing.tools.CaptureSystemOutput.OutputCapture;

import static org.assertj.core.api.Assertions.assertThat;

class PreciseTest {
   @Test
   @CaptureSystemOutput
   void sendSprintFinishedEmail(OutputCapture outputCapture) {

      Precise.sendSprintFinishedEmail("boss@my.corp");

      assertThat(outputCapture.toString())
          .isEqualToNormalizingNewlines("Sending email to boss@my.corp with subject 'Sprint Finished' and some body\n");
   }
}