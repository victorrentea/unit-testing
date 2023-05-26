package victor.testing.filebased.message;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.filebased.FileBasedApprovalTestBase;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("db-mem")
@Transactional
class GroceriesMessageHandlerTest extends FileBasedApprovalTestBase {

  @MockBean
  private KafkaSender kafkaSender;
  @Autowired
  private GroceryRepo groceryRepo;
  @Autowired
  private GroceriesMessageHandler target;
  @Captor
  private ArgumentCaptor<GroceriesResponseMessage> responseMessageCaptor;
  @Captor
  private ArgumentCaptor<GroceryNotFoundEvent> notFoundMessageCaptor;

  public static List<FileTestCase> testData() throws IOException {
    Function<String, String> inToOutFileName = inputFileName -> inputFileName.replace(".in.json", ".out.json");
    return scanForFileTestCases("classpath:/test-cases/message/*.in.json", inToOutFileName);
  }
  // good: well defined input data (vs multiple @Test)
  // bad: many large files. => what if the input/output data structure changes => have to change them all

  @ParameterizedTest
  @MethodSource("testData")
  void handleRequest(FileTestCase testCase) throws IOException {
    Input input = jackson.readValue(testCase.getInputFile(), Input.class);
    Output expectedOutput = jackson.readValue(testCase.getExpectedOutputFile(), Output.class);
    groceryRepo.saveAll(input.groceriesInDb);

    // when
    target.handleRequest(input.request());

    // soft assertions
    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
      // all failures in the soft assertions bellow will be reported

      softly.assertThatCode(() -> verify(kafkaSender)
                      .send(eq("grocery-response"), responseMessageCaptor.capture()))
              .doesNotThrowAnyException();
      softly.assertThat(responseMessageCaptor.getValue())
              .usingRecursiveComparison().isEqualTo(expectedOutput.response);

      softly.assertThatCode(() -> verify(kafkaSender, atLeast(0))
                      .send(eq("grocery-not-found"), notFoundMessageCaptor.capture()))
              .doesNotThrowAnyException();
      softly.assertThat(notFoundMessageCaptor.getAllValues())
              .usingRecursiveFieldByFieldElementComparator()
              .containsExactlyInAnyOrderElementsOf(expectedOutput.notFoundEvents);
    }
  }

  public record Input(GroceriesRequestMessage request,
                      List<Grocery> groceriesInDb) {
  }

  public record Output(GroceriesResponseMessage response,
                       List<GroceryNotFoundEvent> notFoundEvents) {
  }
}