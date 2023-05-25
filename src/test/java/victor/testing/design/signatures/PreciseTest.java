package victor.testing.design.signatures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.tools.CaptureSystemOutput;
import victor.testing.tools.CaptureSystemOutput.OutputCapture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PreciseTest {
   @Mock Project project;
   @Test
   @CaptureSystemOutput
   void sendSprintFinishedEmail(OutputCapture outputCapture) {
//      Project project = new Project();
//      project.setPoEmail("boss@my.corp");
      when(project.getPoEmail()).thenReturn("boss@my.corp"); // PR request: NEVER MOCK GETTERS.
      // we mock behavior not data.
      // we instantiate and fill data

      Precise.sendSprintFinishedEmail(project);

      assertThat(outputCapture.toString()).isEqualToIgnoringNewLines("Sending email to boss@my.corp with subject 'Sprint Finished' and some body");
   }
}