package victor.testing.approval.message;

import lombok.Data;
import org.assertj.core.api.Assertions;
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
import victor.testing.approval.FileBasedApprovalTestBase;
import victor.testing.assertj.SoftAssertions;
import victor.testing.spring.domain.Product;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
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
// THIS TOO!
    public static List<FileTestCase> testData() throws IOException {
        Function<String, String> inToOutFileName = inputFileName -> inputFileName.replace(".in.json", ".out.json");
        return scanForFileTestCases("classpath:/test-cases/message/message*.in.json", inToOutFileName);
    }

    @ParameterizedTest
    @MethodSource("testData")
    void handleRequest(FileTestCase testCase) throws IOException {
        Input input = jackson.readValue(testCase.getInputFile(), Input.class);
        Output expectedOutput = jackson.readValue(testCase.getExpectedOutputFile(), Output.class);
        groceryRepo.saveAll(input.groceriesInDb);
        

        target.handleRequest(input.request());

        verify(kafkaSender, atLeast(0)).send(eq("grocery-not-found"), notFoundMessageCaptor.capture());
        verify(kafkaSender).send(eq("grocery-response"), responseMessageCaptor.capture());
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            // TODO if in the mood to play:
            // softly.assertThatCode(() -> ... ).doesNotThrowAnyException();
            softly.assertThat(responseMessageCaptor.getValue())
                    .usingRecursiveComparison().isEqualTo(expectedOutput.response);
            softly.assertThat(notFoundMessageCaptor.getAllValues()).usingRecursiveFieldByFieldElementComparator()
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