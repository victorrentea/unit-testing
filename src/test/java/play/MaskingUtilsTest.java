package play;

import org.codehaus.plexus.util.cli.Arg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaskingUtilsTest {

  public static List<Arguments> data() throws IOException {
    String input = IOUtils.toString(ClassLoader.getSystemResourceAsStream("sample1.xml"));
    String expected = IOUtils.toString(ClassLoader.getSystemResourceAsStream("sample1-masked.xml"));
    return List.of(
        Arguments.of("<email>a@b.com</email>", "<email>***</email>"),
        Arguments.of("<phone>1234567890</phone>", "<phone>***</phone>"),
        Arguments.of(input, expected)
        // x 20
    );
  }
  @ParameterizedTest
  @MethodSource("data")
  void maskPIIDataFromXml(String input, String expected) {
    MaskingUtils.setPiiMaskingEnabled(true);

    String actual = MaskingUtils.maskPIIDataFromXml(input);

    assertEquals(expected, actual);
  }
}