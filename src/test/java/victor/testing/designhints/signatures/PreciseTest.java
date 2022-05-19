package victor.testing.designhints.signatures;

import org.junit.jupiter.api.Test;
import victor.testing.tools.CaptureSystemOutput;
import victor.testing.tools.CaptureSystemOutput.OutputCapture;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PreciseTest {
   @Test
   @CaptureSystemOutput
   void sendSprintFinishedEmail(OutputCapture outputCapture) {
      Project project = new Project(12L, "useless", "i dont care", "boss@my.corp", LocalDate.now());
//      project.setPoEmail("boss@my.corp");

      Precise.sendSprintFinishedEmail("boss@my.corp");

      assertThat(outputCapture.toString()).isEqualToIgnoringNewLines("Sending email to boss@my.corp with subject 'Sprint Finished' and some body");
   }
}