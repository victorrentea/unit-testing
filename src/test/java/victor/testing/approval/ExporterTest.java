package victor.testing.approval;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.FileAssert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ExporterTest extends FileBasedApprovalTestBase {

   @Mock
   private PersonRepo personRepo;

   @InjectMocks
   private PersonExporter exporter;
   public static final ObjectMapper jackson = new ObjectMapper().registerModule(new JavaTimeModule());

   public static List<FileTestCase> convert() throws IOException {
      return scanForFileTestCases("classpath:/test-cases/export*.in.json",
          inputFileName -> inputFileName.replace(".in.json", ".out.csv"));
   }
   @ParameterizedTest
   @MethodSource // default: call a static method with the same name
   public void convert(FileTestCase test) throws IOException {
      log.info("Running {}", test);
      didacticLog(test);

      Person inputPerson = jackson.readValue(test.getInputFile(), Person.class);
      when(personRepo.findAll()).thenReturn(asList(inputPerson));

      StringWriter sw = new StringWriter();
      exporter.export(sw);

      String expectedContents = readFileToString(test.getExpectedOutputFile());
      Assertions.assertThat(sw.toString()).isEqualToNormalizingNewlines(expectedContents);
   }

   private void didacticLog(FileTestCase testCase) throws IOException {
      String inStr = readFileToString(testCase.getInputFile());
      String outStr = readFileToString(testCase.getExpectedOutputFile());

      System.out.println("Running with input:\n" + inStr);
      System.out.println("\nExpecting output:\n" + outStr);
   }


}