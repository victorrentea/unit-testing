package victor.testing.tdd.unusualspending;

import java.time.LocalDate;
import java.util.List;

public class UnusualSpendingService {
    private final PaymentServiceClient paymentServiceClient;
    private final PaymentsAnalyzer paymentsAnalyzer;
    private final EmailSender emailSender;

    public UnusualSpendingService(PaymentServiceClient paymentServiceClient, PaymentsAnalyzer paymentsAnalyzer, EmailSender emailSender) {
        this.paymentServiceClient = paymentServiceClient;
        this.paymentsAnalyzer = paymentsAnalyzer;
        this.emailSender = emailSender;
    }

    public void checkClient(String userId) {
        LocalDate now = LocalDate.now();

        List<Payment> payments = paymentServiceClient.fetchPayments(userId,
                now.getYear(), now.getMonthValue());

        UnsualPaymentReport report = paymentsAnalyzer.analyze(payments);

        emailSender.sendUnusualSpendingReport(report);
    }
}
