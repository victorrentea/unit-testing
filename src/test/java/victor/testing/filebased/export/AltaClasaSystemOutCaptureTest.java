package victor.testing.filebased.export;

import org.junit.jupiter.api.Test;
import victor.testing.tools.CaptureSystemOutput;

import static org.assertj.core.api.Assertions.assertThat;

class AltaClasaSystemOutCaptureTest {

    @Test
    @CaptureSystemOutput
    void method(CaptureSystemOutput.OutputCapture capture) {

        new AltaClasa()
                .method(true);

        assertThat(capture.toString()).contains("CEA MAI PROASTA IDEE");
    }
}