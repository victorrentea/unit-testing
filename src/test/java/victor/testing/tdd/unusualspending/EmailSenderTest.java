package victor.testing.tdd.unusualspending;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.publisher.PublisherProbe;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailSenderTest {
    private static final String USER_ID = "jdoe";
    @Mock
    EmailClient emailClient;
    @InjectMocks
    EmailSender emailSender;

    @Test
    void sendUnusualSpendingEmail() {
        UnusualSpendingReport report = new UnusualSpendingReport(150, Map.of(
                Category.GROCERIES, 50, Category.TRAVEL, 100));
        PublisherProbe<Void> sendMailProbe = PublisherProbe.empty();
        when(emailClient.sendEmail(USER_ID, "Unusual spending of $150 detected!", """
                Hello card user!
                                
                We have detected unusually high spending on your card in these categories:
                                
                * You spent $50 on groceries
                * You spent $100 on travel
                                
                Love,
                                
                The Credit Card Company
                """)).thenReturn(sendMailProbe.mono());

        emailSender.sendUnusualSpendingEmail(report, USER_ID).block();

        sendMailProbe.assertWasSubscribed();
    }
}