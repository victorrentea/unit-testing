package victor.testing.spring.export;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
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
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ProductExporterTest extends FileBasedApprovalTestBase {
  public static final ObjectMapper jackson = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES)) // allows to even remove default private constructor!
      ;
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
    Product[] inputProduct = jackson.readValue(testCase.inputFile(), Product[].class);
    when(personRepo.findAll()).thenReturn(asList(inputProduct));
    StringWriter sw = new StringWriter();

    // when
    exporter.writeContent(sw);

    String expectedContents = readFileToString(testCase.expectedOutputFile());
    assertThat(sw.toString()).isEqualToNormalizingNewlines(expectedContents);
  }

  private void didacticLog(FileTestCase testCase) throws IOException {
    String inStr = readFileToString(testCase.inputFile());
    String outStr = readFileToString(testCase.expectedOutputFile());
    log.info("Running {}", testCase);
    System.out.println("Running with input:\n" + inStr);
    System.out.println("\nExpecting output:\n" + outStr);
  }
}