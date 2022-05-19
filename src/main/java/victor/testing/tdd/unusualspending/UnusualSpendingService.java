package victor.testing.tdd.unusualspending;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
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

        // not pure because networking
        Set<Payment> paymentsThisMonth = paymentServiceClient.fetchPayments(userId, YearMonth.from(now));
        Set<Payment> paymentsLastMonth = paymentServiceClient.fetchPayments(userId, YearMonth.from(now.minusMonths(1)));

        Set<Payment> payments = Stream.concat(paymentsThisMonth.stream(), paymentsLastMonth.stream()).collect(toSet());

        // pure part
        Optional<UnsualPaymentReport> reportOpt = paymentsAnalyzer.analyze(payments); // puere function

        // side effect part
        reportOpt.ifPresent(report -> emailSender.sendUnusualSpendingReport(report));

        // TODO Think : purify even more ?
//        Email e = composeEmail(report);
//        email clinent. send(e);
    }
}

@Component
class TimeProvider {
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
