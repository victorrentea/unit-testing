package victor.testing.approval;

import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ApprovalFileBasedTest {
   @Value
   private static class FileTestCase {
      File inputFile;
      File expectedOutputFile;
   }
   @ParameterizedTest
   @MethodSource
   public void convert(FileTestCase testCase) throws IOException {
      String inStr = FileUtils.readFileToString(testCase.getInputFile());
      String outStr = FileUtils.readFileToString(testCase.getExpectedOutputFile());

//      new ObjectMapper().readValue() TODO ....
      System.out.println("Running with input : " + inStr);
      System.out.println("Expecting output : " + outStr);
   }

   public static List<FileTestCase> convert() throws IOException {
      Resource[] inResources = new PathMatchingResourcePatternResolver().getResources("classpath:/test-cases/*.in.json");
      return Stream.of(inResources)
          .map(ApprovalFileBasedTest::constructArguments)
          .collect(toList());
   }

   @SneakyThrows
   private static FileTestCase constructArguments(Resource inResource) {
      String outFileName = inResource.getFilename().replace(".in.", ".out.");
      Resource outResource = inResource.createRelative(outFileName);
      if (!outResource.exists()) {
         throw new IllegalArgumentException("No matching file found for "  + inResource.getFilename() + ". Expected out filename = " + outResource.getFile().getAbsolutePath());
      }
      return new FileTestCase(inResource.getFile(), outResource.getFile());
   }
}