package victor.testing.tdd.unusualspending;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;
import victor.testing.tools.WireMockExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "payment.base.url=http://localhost:9999")
//@ExtendWith(MockitoExtension.class)
@ActiveProfiles("db-mem")
public class UnusualSpendingServiceTest {
    private static final String USER_ID = "jdoe";
//    @MockBean
//    PaymentFetcher paymentFetcher;
    @MockBean
    TimeProvider timeProvider;
//    @MockBean
//    PaymentAnalyzer paymentAnalyzer;
    @MockBean
    EmailClient emailClient;
//    @MockBean
//    EmailSender emailSender;
    @Autowired
    UnusualSpendingService unusualSpendingService;
    @RegisterExtension
    public WireMockExtension wireMockExtension = new WireMockExtension(9999);

    private final PublisherProbe<Void> sendEmailProbe = PublisherProbe.empty();

    @Test
    void normalFlow() {
        when(timeProvider.currentLocalDate()).thenReturn(LocalDate.parse("2013-11-25"));
//        when(emailSender.sendUnusualSpendingEmail(any(), USER_ID)).thenReturn(sendEmailProbe.mono());
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        when(emailClient.sendEmail(eq(USER_ID), subjectCaptor.capture(), bodyCaptor.capture())).thenReturn(sendEmailProbe.mono());

        unusualSpendingService.checkSpending(USER_ID)
                .as(StepVerifier::create)
                .verifyComplete();

        sendEmailProbe.assertWasSubscribed();
        assertThat(subjectCaptor.getValue()).isEqualTo("Unusual spending of $1076 detected!");
        assertThat(bodyCaptor.getValue()).isEqualTo("Hello card user!\n" +
                "\n" +
                "We have detected unusually high spending on your card in these categories:\n" +
                "\n" +
                "* You spent $928 on travel\n" +
                "* You spent $148 on groceries\n" +
                "\n" +
                "Love,\n" +
                "\n" +
                "The Credit Card Company\n");
    }

    @Test
    void shouldNotSendEmail_whenNothingUnusual() {
//        Payment payment = new Payment(10, Category.ENTERTAINMENT, LocalDate.now().minusMonths(1));
//        when(paymentFetcher.fetchLastTwoMonthsPayments(USER_ID)).thenReturn(Flux.just(payment));
//        when(paymentAnalyzer.analyze(List.of(payment))).thenReturn(empty());
//
//        unusualSpendingService.checkSpending(USER_ID)
//                .as(StepVerifier::create)
//                .verifyComplete();
//
//        verify(emailSender, never()).sendUnusualSpendingEmail(any());
    }


    void ignoreDuplicatedChecksPerMonth() { // TODO
    }
}
