package victor.testing.design.signatures;

import org.junit.jupiter.api.Test;
import victor.testing.design.tools.CaptureSystemOutput;
import victor.testing.design.tools.CaptureSystemOutput.OutputCapture;

import static org.assertj.core.api.Assertions.assertThat;

class SignaturesTest {
   @Test
   @CaptureSystemOutput
   void sendSprintFinishedEmail(OutputCapture outputCapture) {
      Signatures.sendSprintFinishedEmail("boss@my.corp");

      assertThat(outputCapture.toString()).isEqualToIgnoringNewLines("Sending email to boss@my.corp with subject 'Sprint Finished' ... ");
   }
}