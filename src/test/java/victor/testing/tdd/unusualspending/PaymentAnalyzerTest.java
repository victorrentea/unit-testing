package victor.testing.tdd.unusualspending;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentAnalyzerTest {
    public static final LocalDate NOW = LocalDate.parse("2013-11-25");
    public static final LocalDate LAST_MONTH = NOW.minusMonths(1);
    public static final LocalDate THIS_MONTH = NOW;
    @InjectMocks
    PaymentAnalyzer paymentAnalyzer;
    @Mock
    TimeProvider timeProvider;
    @BeforeEach
    final void before() {
        when(timeProvider.currentLocalDate()).thenReturn(NOW);
    }
    @Test
    void analyze() {
        List<Payment> payments = List.of(
                new Payment(10, Category.ENTERTAINMENT, LAST_MONTH),
                new Payment(16, Category.ENTERTAINMENT, THIS_MONTH));

        UnusualSpendingReport report = paymentAnalyzer.analyze(payments).get();

        assertThat(report.amountPerCategory()).containsEntry(Category.ENTERTAINMENT, 16);
        assertThat(report.total()).isEqualTo(16);
    }
    @Test
    void ignoresWhenNothingInPreviousMonth() {
        List<Payment> payments = List.of(
                new Payment(16, Category.ENTERTAINMENT, THIS_MONTH));

        assertThat(paymentAnalyzer.analyze(payments)).isEmpty();
    }
    @Test
    void ignoresWhenNothingInCurrentMonth() {
        List<Payment> payments = List.of(
                new Payment(16, Category.ENTERTAINMENT, LAST_MONTH));

        assertThat(paymentAnalyzer.analyze(payments)).isEmpty();
    }
    @Test
    void sumsUpPerCategory() {
        List<Payment> payments = List.of(
                new Payment(5, Category.ENTERTAINMENT, LAST_MONTH),
                new Payment(5, Category.ENTERTAINMENT, LAST_MONTH),
                new Payment(16, Category.ENTERTAINMENT, THIS_MONTH));

        UnusualSpendingReport report = paymentAnalyzer.analyze(payments).get();

        assertThat(report.amountPerCategory()).containsEntry(Category.ENTERTAINMENT, 16);
        assertThat(report.total()).isEqualTo(16);
    }
    @Test
    void sumsUpGrandTotal() {
        List<Payment> payments = List.of(
                new Payment(10, Category.ENTERTAINMENT, LAST_MONTH),
                new Payment(16, Category.ENTERTAINMENT, THIS_MONTH),
                new Payment(10, Category.GOLF, LAST_MONTH),
                new Payment(16, Category.GOLF, THIS_MONTH)
        );

        UnusualSpendingReport report = paymentAnalyzer.analyze(payments).get();

        assertThat(report.total()).isEqualTo(32);
    }
    @Test
    void returnsEmpty_whenNothingUnusual() {
        List<Payment> payments = List.of(
                new Payment(14, Category.ENTERTAINMENT, LAST_MONTH),
                new Payment(16, Category.ENTERTAINMENT, THIS_MONTH));

        assertThat(paymentAnalyzer.analyze(payments)).isEmpty();
    }
}