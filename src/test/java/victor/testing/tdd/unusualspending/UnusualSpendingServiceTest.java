package victor.testing.tdd.unusualspending;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.*;

// outside-in TDD: we start top-level and we dig in by
// putting mocks instead of the collaborators, until they are ready.
@ExtendWith(MockitoExtension.class)
public class UnusualSpendingServiceTest {
    public static final String USER_ID = "jdoe";
    @Mock
    TimeProvider timeProvider;
    @Mock
    PaymentServiceClient paymentServiceClient;
    @Mock
    PaymentsAnalyzer paymentsAnalyzer;
    @Mock
    EmailSender emailSender;
    @InjectMocks
    UnusualSpendingService unusualSpendingService;

    // "Fake it till you make it"
    @BeforeEach
    final void before() {
        when(timeProvider.getCurrentDate()).thenReturn(LocalDate.parse("2013-11-25"));
    }
    @Test
    void acceptanceTest() { // FRAGILE TEST. FULL OF MOCKS, BUT NOT TESTING REALLY ANY SERIOUS LOGIC IN PROD
        UnsualPaymentReport report = new UnsualPaymentReport(Map.of(Category.GROCERIES, 100));
        Payment novPayment = new Payment(100, Category.GROCERIES, LocalDate.parse("2013-11-20"));
        Payment octPayment = new Payment(20, Category.GROCERIES, LocalDate.parse("2013-10-20"));
        when(paymentServiceClient.fetchPayments(USER_ID, YearMonth.of(2013, 10))).thenReturn(Set.of(octPayment));
        when(paymentServiceClient.fetchPayments(USER_ID, YearMonth.of(2013, 11))).thenReturn(Set.of(novPayment));
        when(paymentsAnalyzer.analyze(Set.of(novPayment, octPayment))).thenReturn(of(report));

        unusualSpendingService.checkClient(USER_ID);

//        verify(paymentServiceClient).fetchPayments(USER_ID, YearMonthof(2013, 11);)
        verify(emailSender).sendUnusualSpendingReport(report);
    }

    @Test
    void whatIfThereIsNoReport() {
        when(paymentServiceClient.fetchPayments(USER_ID, YearMonth.of(2013, 11))).thenReturn(Collections.emptySet());
        when(paymentsAnalyzer.analyze(Collections.emptySet())).thenReturn(empty());

        unusualSpendingService.checkClient(USER_ID);

        verifyNoInteractions(emailSender);
    }

}
