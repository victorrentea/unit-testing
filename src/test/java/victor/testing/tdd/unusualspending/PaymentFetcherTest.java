package victor.testing.tdd.unusualspending;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import victor.testing.tools.WireMockExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentFetcherTest {
    private static final String USER_ID = "jdoe";
    @InjectMocks
    PaymentFetcher paymentFetcher;
//    @Mock
//    PaymentServiceClient paymentServiceClient;
    @Mock
    TimeProvider timeProvider;

    @RegisterExtension
    public WireMockExtension wireMockExtension = new WireMockExtension(9999);

    @BeforeEach
    final void before() {
        paymentFetcher.baseUrl = "http://localhost:9999";
    }

    @Test
    void fetchLastTwoMonthsPayments() {
        when(timeProvider.currentLocalDate()).thenReturn(LocalDate.parse("2013-11-25"));

        List<Payment> payments = paymentFetcher.fetchLastTwoMonthsPayments(USER_ID)
                .collectList().block();

        assertThat(payments).containsExactlyInAnyOrderElementsOf(List.of(
                new Payment(100, Category.GROCERIES, LocalDate.parse("2013-11-05")),
                new Payment(48, Category.GROCERIES, LocalDate.parse("2013-11-06")),
                new Payment(60, Category.GROCERIES, LocalDate.parse("2013-10-05")),
                new Payment(500, Category.TRAVEL, LocalDate.parse("2013-10-30")),
                new Payment(928, Category.TRAVEL, LocalDate.parse("2013-11-30"))
        ));
    }
}