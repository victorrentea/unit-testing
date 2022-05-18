package victor.testing.tdd.unusualspending;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class UnusualSpendingService {
    private final PaymentServiceClient paymentServiceClient;
    private final PaymentsAnalyzer paymentsAnalyzer;
    private final EmailSender emailSender;
    private final TimeProvider timeProvider;

    public UnusualSpendingService(PaymentServiceClient paymentServiceClient, PaymentsAnalyzer paymentsAnalyzer, EmailSender emailSender, TimeProvider timeProvider) {
        this.paymentServiceClient = paymentServiceClient;
        this.paymentsAnalyzer = paymentsAnalyzer;
        this.emailSender = emailSender;
        this.timeProvider = timeProvider;
    }

    public void checkClient(String userId) {
        LocalDate now = timeProvider.getCurrentDate();

        Set<Payment> paymentsThisMonth = paymentServiceClient.fetchPayments(userId, YearMonth.from(now));

        Set<Payment> paymentsLastMonth = paymentServiceClient.fetchPayments(userId,
                YearMonth.from(now.minusMonths(1)));
//         paymentServiceClient.fetchPayments(userId, now.getYear(), now.getMonthValue());

        Set<Payment> payments = Stream.concat(paymentsThisMonth.stream(), paymentsLastMonth.stream()).collect(toSet());

        paymentsAnalyzer.analyze(payments).ifPresent(report -> emailSender.sendUnusualSpendingReport(report));
    }
}

@Component
class TimeProvider {
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
