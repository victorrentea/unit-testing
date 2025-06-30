package victor.testing.tools;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public abstract class FileApprovalTestBase {
  public static List<FileTestCase> scanForFileTestCases(String locationPattern, Function<String, String> outputFileNameProvider) throws IOException {
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
    String commonFileNamePrefix = StringUtils.getCommonPrefix(inputFileName, outFileName);
    String testDisplayName = commonFileNamePrefix.replaceAll("-+", " ");
    if (!outResource.exists()) {
      throw new IllegalArgumentException("No matching file found for " + inResource.getFilename() + ". Expected out filename = " + outResource.getFile().getAbsolutePath());
    }
    return new FileTestCase(testDisplayName.substring(0,testDisplayName.length()-1), inResource.getFile(), outResource.getFile());
  }

  public record FileTestCase(String displayName, File inputFile, File expectedOutputFile) {
    @Override
    public String toString() {
      return displayName + " (" + inputFile.getName() + " -> " + expectedOutputFile.getName() + ")";
    }
  }
}
