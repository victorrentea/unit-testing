package victor.testing.spring.export;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.spring.entity.Product;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.tools.FileBasedApprovalTestBase;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ProductExporterTest extends FileBasedApprovalTestBase {
   @Mock
   private ProductRepo personRepo;

   @InjectMocks
   private ProductExporter exporter;

   public static List<FileTestCase> testData() throws IOException {
      Function<String, String> inToOutFileName = inputFileName -> inputFileName.replace(".in.json", ".out.csv");
      return scanForFileTestCases("classpath:/test-cases/export/*.in.json", inToOutFileName);
   }

   @ParameterizedTest(name = "{0}")
   @MethodSource("testData")
   public void convert(FileTestCase testCase) throws IOException {
      didacticLog(testCase);
      Product[] inputProduct = jackson.readValue(testCase.getInputFile(), Product[].class);
      when(personRepo.findAll()).thenReturn(asList(inputProduct));
      StringWriter sw = new StringWriter();

      // when
      exporter.writeContent(sw);

      String expectedContents = readFileToString(testCase.getExpectedOutputFile());
      assertThat(sw.toString()).isEqualToNormalizingNewlines(expectedContents);
   }

   private void didacticLog(FileTestCase testCase) throws IOException {
      String inStr = readFileToString(testCase.getInputFile());
      String outStr = readFileToString(testCase.getExpectedOutputFile());
      log.info("Running {}", testCase);
      System.out.println("Running with input:\n" + inStr);
      System.out.println("\nExpecting output:\n" + outStr);
   }
}