package victor.testing.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.SneakyThrows;
import lombok.Value;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FileBasedApprovalTestBase {
   public static final ObjectMapper jackson = new ObjectMapper()
       .registerModule(new JavaTimeModule())
       .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES)) // allows to even remove default private constructor!
       ;

   protected static List<FileTestCase> scanForFileTestCases(String locationPattern, Function<String, String> outputFileNameProvider) throws IOException {
      Resource[] inResources = new PathMatchingResourcePatternResolver().getResources(locationPattern);
      return Stream.of(inResources)
          .map(inResource -> constructArguments(inResource, outputFileNameProvider))
          .collect(toList());
   }

   @SneakyThrows
   private static FileTestCase constructArguments(Resource inResource, Function<String, String> outputFileNameProvider) {
      String inputFileName = inResource.getFilename();
      String outFileName = outputFileNameProvider.apply(inputFileName);
      Resource outResource = inResource.createRelative(outFileName);
      if (!outResource.exists()) {
         throw new IllegalArgumentException("No matching file found for " + inResource.getFilename() + ". Expected out filename = " + outResource.getFile().getAbsolutePath());
      }
      return new FileTestCase(inResource.getFile(), outResource.getFile());
   }
   @Value
   protected static class FileTestCase {
      File inputFile;
      File expectedOutputFile;

      @Override
      public String toString() {
         return  inputFile.getName() + " --> " + expectedOutputFile.getName();
      }
   }
}
