package victor.testing.tdd.unusualspending;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// outside-in TDD: we start top-level and we dig in by
// putting mocks instead of the collaborators, until they are ready.
@ExtendWith(MockitoExtension.class)
public class UnusualSpendingServiceTest {
    public static final String USER_ID = "jdoe";
    @Mock
    PaymentServiceClient paymentServiceClient;
    @Mock
    PaymentsAnalyzer paymentsAnalyzer;
    @Mock
    EmailSender emailSender;
    @InjectMocks
    UnusualSpendingService unusualSpendingService;

    // "Fake it till you make it"
    
    @Test
    void acceptanceTest() {
        UnsualPaymentReport report = new UnsualPaymentReport(Map.of(Category.GROCERIES, 10));
        List<Payment> payments = List.of(new Payment(10, Category.GROCERIES));
        Mockito.when(paymentServiceClient.fetchPayments(USER_ID, 2022, 5)).thenReturn(payments);
        when(paymentsAnalyzer.analyze(payments)).thenReturn(Optional.of(report));

        unusualSpendingService.checkClient(USER_ID);

        verify(emailSender).sendUnusualSpendingReport(report);
    }

    @Test
    @Disabled
    void whatIfThereIsNoReport() {

    }
}
