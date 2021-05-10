package victor.testing.approval;

import lombok.Value;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.List;

public class ApprovalFileBasedTest {
   @Value
   private static class FileTestCase {
      String name;
      File input;
      File expectedOutput;
   }
   @ParameterizedTest
   @MethodSource
   public void convert(String name, File input, File expectedOutput) {

   }
//   public static List<Arguments> convert() {
//      ApprovalFileBasedTest.class.getResource("test-cases");
//   }
}
