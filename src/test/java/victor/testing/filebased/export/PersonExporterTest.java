package victor.testing.filebased.export;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.filebased.FileBasedApprovalTestBase;

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
public class PersonExporterTest extends FileBasedApprovalTestBase {
   @Mock
   private PersonRepo personRepo;

   @InjectMocks
   private PersonExporter exporter;

   public static List<FileTestCase> testData() throws IOException {
      Function<String, String> inToOutFileName = inputFileName -> inputFileName.replace(".in.json", ".out.csv");
      return scanForFileTestCases("classpath:/test-cases/export/export*.in.json", inToOutFileName);
   }
   @ParameterizedTest
   @MethodSource("testData")
   public void convert(FileTestCase test) throws IOException {
      log.info("Running {}", test);
      didacticLog(test);
      Person inputPerson = jackson.readValue(test.getInputFile(), Person.class);
      when(personRepo.findAll()).thenReturn(asList(inputPerson));
      StringWriter sw = new StringWriter();

      // when
      exporter.export(sw);

      String expectedContents = readFileToString(test.getExpectedOutputFile());
      assertThat(sw.toString()).isEqualToNormalizingNewlines(expectedContents);
   }

   private void didacticLog(FileTestCase testCase) throws IOException {
      String inStr = readFileToString(testCase.getInputFile());
      String outStr = readFileToString(testCase.getExpectedOutputFile());

      System.out.println("Running with input:\n" + inStr);
      System.out.println("\nExpecting output:\n" + outStr);
   }


}