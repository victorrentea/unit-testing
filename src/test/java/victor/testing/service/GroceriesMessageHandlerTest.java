package victor.testing.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftlyExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import victor.testing.entity.Grocery;
import victor.testing.service.GroceriesMessageHandler.GroceriesReplyMessage;
import victor.testing.service.GroceriesMessageHandler.GroceriesRequestMessage;
import victor.testing.service.GroceriesMessageHandler.GroceryNotFoundEvent;
import victor.testing.repo.GroceryRepo;
import victor.testing.tools.FileApprovalTestBase.FileTestCase;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static victor.testing.tools.FileApprovalTestBase.scanForFileTestCases;

@SuppressWarnings("ALL")
@ExtendWith({MockitoExtension.class, SoftlyExtension.class})
class GroceriesMessageHandlerTest {
  public static final ObjectMapper jackson = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES)) // allows to remove default private constructor!
      ;
  @InjectSoftAssertions
  SoftAssertions softly;
  @Mock
  private KafkaTemplate kafkaSender;
  @Mock
  private GroceryRepo groceryRepo;
  @InjectMocks
  private GroceriesMessageHandler target;
  @Captor
  private ArgumentCaptor<GroceriesReplyMessage> responseMessageCaptor;
  @Captor
  private ArgumentCaptor<GroceryNotFoundEvent> notFoundMessageCaptor;

  public static List<FileTestCase> testData() throws IOException {
    Function<String, String> inToOutFileName = inputFileName -> inputFileName.replace(".in.json", ".out.json");
    return scanForFileTestCases("classpath:/test-cases/message/*.in.json", inToOutFileName);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testData")
  void handleRequest(FileTestCase testCase) throws IOException {
    Input input = jackson.readValue(testCase.inputFile(), Input.class);
    Output expectedOutput = jackson.readValue(testCase.expectedOutputFile(), Output.class);
    when(groceryRepo.findByName(any())).thenAnswer(invocation -> {
      return input.groceriesInDb.stream()
          .filter(g -> g.getName().equals(invocation.<String>getArgument(0)))
          .findFirst();
    });

    target.handleRequest(input.request());

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

  public record Input(GroceriesRequestMessage request,
                      List<Grocery> groceriesInDb) {
  }

  public record Output(GroceriesReplyMessage response,
                       List<GroceryNotFoundEvent> notFoundEvents) {
  }
}