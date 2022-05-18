package victor.testing.tdd.unusualspending;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Component
public class PaymentFetcher {
    private final TimeProvider timeProvider;

    @Value("${payment.base.url}")
    String baseUrl;

    public PaymentFetcher(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public Flux<Payment> fetchLastTwoMonthsPayments(String userId) {
        LocalDate now = timeProvider.currentLocalDate();
        System.out.println("Using date=" + now);
        return fetchPaymentsForMonth(userId, now.getYear(), now.getMonthValue())
                .mergeWith(fetchPaymentsForMonth(userId, now.minusMonths(1).getYear(), now.minusMonths(1).getMonthValue()))
                .map(this::fromDto);
    }

    private Flux<PaymentDto> fetchPaymentsForMonth(String userId, int year, int month) {
        return WebClient.create()
                .get().uri(baseUrl + "/payments/{username}/{year}/{month}", userId, year, month)
                .retrieve()
                .bodyToFlux(PaymentDto.class);
//        return paymentServiceClient.fetchPayments(userId, timeProvider.getMonthValue());
    }

    private Payment fromDto(PaymentDto dto) {
        return new Payment(dto.amount(), dto.category(), dto.date());
    }
}
