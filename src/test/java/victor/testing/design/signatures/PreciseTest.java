package victor.testing.design.signatures;

import org.junit.jupiter.api.Test;
import victor.testing.tools.CaptureSystemOutput;
import victor.testing.tools.CaptureSystemOutput.OutputCapture;

import static org.assertj.core.api.Assertions.assertThat;

class PreciseTest {
   @Test
   @CaptureSystemOutput
   void sendSprintFinishedEmail(OutputCapture outputCapture) {
      // happier test
      Precise.sendSprintFinishedEmail("boss@my.corp");

      assertThat(outputCapture.toString())
          .isEqualToIgnoringNewLines("Sending email to boss@my.corp with subject 'Sprint Finished' and some body");
   }



//   // whenever a test is hard to write, you can ALWAYS SHOVEL SOME SH*T INTO A HELPER METHOD.
//   //
//   //But here there is a simpler solution to the problem, that also improves the production design.
//   private static Project projectWithPOEmail(String mail) {
//      Project project = new Project();
//      project.setPoEmail(mail);
//      return project;
//   }
}