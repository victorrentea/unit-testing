package victor.testing.tdd.unusualspending;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UnusualSpendingService {
    private final EmailSender emailSender;
    private final PaymentAnalyzer paymentAnalyzer;
    private final PaymentFetcher paymentFetcher;

    public UnusualSpendingService(EmailSender emailSender, PaymentAnalyzer paymentAnalyzer, PaymentFetcher paymentFetcher) {
        this.emailSender = emailSender;
        this.paymentAnalyzer = paymentAnalyzer;
        this.paymentFetcher = paymentFetcher;
    }

    public Mono<Void> checkSpending(String userId) {
        return paymentFetcher.fetchLastTwoMonthsPayments(userId)
                .collectList()
                .mapNotNull(payments -> paymentAnalyzer.analyze(payments).orElse(null))
                .flatMap(unusualSpendingReport -> emailSender.sendUnusualSpendingEmail(unusualSpendingReport, userId));
    }
}
